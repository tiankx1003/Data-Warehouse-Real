package com.tian.dw.gmall.realtime.app

import com.alibaba.fastjson.JSON
import com.tian.dw.gmall.realtime.util.{MyKafkaUtil, RedisUtil}
import org.apache.spark.SparkConf
import com.tian.dw.gmall.realtime.bean.StartupLog
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis
import com.tian.gmall.common.ConstantUtil

/**
 *
 * @author tian
 * @version 1.0
 *          2019/10/8 9:06
 */
object DauApp {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DauApp")
        val ssc = new StreamingContext(conf, Seconds(5))
        val sourceStream: InputDStream[(String, String)] =
            MyKafkaUtil.getKafkaStream(ssc, ConstantUtil.STARTUP_TOPIC)

        // 1. 调整数据结构
        val startupLogDSteam = sourceStream.map {
            case (_, log) => JSON.parseObject(log, classOf[StartupLog])
        }
        // 2. 保存到 redis
        startupLogDSteam.foreachRDD(rdd => {
            rdd.foreachPartition(it => {
                val client: Jedis = RedisUtil.getJedisClient
                it.foreach(startupLog => {
                    // 存入到 Redis value 类型 set, 存储 uid
                    val key = "dau:" + startupLog.logDate
                    client.sadd(key, startupLog.uid)
                })
                client.close()
            })
        })
        ssc.start()
        ssc.awaitTermination()
    }
}

