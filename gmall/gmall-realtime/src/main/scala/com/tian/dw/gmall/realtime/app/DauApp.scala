package com.tian.dw.gmall.realtime.app

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.alibaba.fastjson.JSON
import com.tian.dw.gmall.realtime.util.{MyKafkaUtil, RedisUtil}
import org.apache.spark.SparkConf
import com.tian.dw.gmall.realtime.bean.StartupLog
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis
import com.tian.gmall.common.ConstantUtil
import org.apache.spark.broadcast.Broadcast

/**
 * 数据去重并写入到HBase
 *
 * @author tian
 * @version 1.0
 *          2019/10/8 9:06
 */
object DauApp {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setAppName("DauApp").setMaster("local[2]")
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
        val sourceDStream: InputDStream[(String, String)] = MyKafkaUtil.getKafkaStream(ssc, ConstantUtil.STARTUP_TOPIC)

        // 1. 调整数据结构并封装数据
        val startupLogDSteam: DStream[StartupLog] = sourceDStream.map {
            case (_, value) =>
                val log: StartupLog = JSON.parseObject(value, classOf[StartupLog])
                log.logDate = new SimpleDateFormat("yyyy-MM-dd").format(log.ts)
                log.logHour = new SimpleDateFormat("HH").format(log.ts)
                log
        }

        // 2. 保存到 redis
        // 2.1 过滤已经启动过的设备
        val filteredDSteam: DStream[StartupLog] = startupLogDSteam
            .transform(rdd => {
                // 2.1.1 读取已经启动过得设备
                val client: Jedis = RedisUtil.getJedisClient
                val uidSet: util.Set[String] =
                    client.smembers(ConstantUtil.STARTUP_TOPIC + ":"
                        + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                val uidSetBC: Broadcast[util.Set[String]] = ssc.sparkContext.broadcast(uidSet)
                client.close()
                // 2.1.2 过滤已经启动过得设备
                rdd.filter(log => {
                    !uidSetBC.value.contains(log.uid)
                })
            })
            // 2.1.3 某个用户可能在第一次启动所在的批次中出现该用户多次启动
            .map(log => (log.uid, log))
            .groupByKey
            .flatMap {
                case (_, logIt) => logIt.toList.sortBy(_.ts).take(1)
            }

        // 2.2 写入到redis中，只写uid(已经启动过得用户用户设备)
        filteredDSteam
            .foreachRDD(rdd => {
                rdd.foreachPartition(it => {
                    val client: Jedis = RedisUtil.getJedisClient
                    it.foreach(startupLog => {
                        // 存入到 Redis value 类型 set, 存储 uid
                        val key = ConstantUtil.STARTUP_TOPIC + ":" + startupLog.logDate
                        client.sadd(key, startupLog.uid)
                    })
                    client.close()
                })
            })

        // 3. 写入到HBase
        import org.apache.phoenix.spark._
        filteredDSteam
            .foreachRDD(rdd => {
                rdd.saveToPhoenix(
                    "GMALL_DAU",
                    Seq("MID", "UID", "APPID", "AREA", "OS", "CH", "TYPE", "VS", "TS", "LOGDATE", "LOGHOUR"),
                    zkUrl = Some("hadoop102,hadoop103,hadoop104:2181")
                )
            })

        ssc.start()
        ssc.awaitTermination()
    }
}

