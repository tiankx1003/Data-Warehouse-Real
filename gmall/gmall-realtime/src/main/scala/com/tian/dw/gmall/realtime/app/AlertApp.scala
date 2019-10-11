package com.tian.dw.gmall.realtime.app

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.alibaba.fastjson.JSON
import com.tian.gmall.common.ConstantUtil
import com.tian.dw.gmall.realtime.bean.{AlertInfo, EventLog}
import com.tian.dw.gmall.realtime.util.{MyESUtil, MyKafkaUtil}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.util.control.Breaks._

/**
 * @author tian
 * @date 2019-10-10 14:43:47
 * @version 1.0
 */
object AlertApp {
    def main(args: Array[String]): Unit = {
        // 1. 从kafka消费数据(事件日志)
        val conf: SparkConf = new SparkConf().setAppName("DAUApp").setMaster("local[1]")
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
        val sourceDStream: InputDStream[(String, String)] = MyKafkaUtil.getKafkaStream(ssc, ConstantUtil.EVENT_TOPIC)

        // 2. 添加窗口, 调整数据结构
        val eventLogDStream: DStream[(String, EventLog)] = sourceDStream
            .window(Seconds(5 * 60), Seconds(5))
            .map {
                case (_, jsonString) =>
                    val log: EventLog = JSON.parseObject(jsonString, classOf[EventLog])
                    val date: Date = new Date(log.ts)
                    log.logDate = new SimpleDateFormat("yyyy-MM-dd").format(date)
                    log.logHour = new SimpleDateFormat("HH").format(date)
                    (log.mid, log)
            }

        // 3. 按照 uid 分组
        val groupedEventLogDStream: DStream[(String, Iterable[EventLog])] = eventLogDStream.groupByKey

        // 4. 预警的业务逻辑
        val checkCouponAlertDStream: DStream[(Boolean, AlertInfo)] = groupedEventLogDStream.map {
            case (mid, logIt) =>
                val uids: util.HashSet[String] = new util.HashSet[String]()
                val itemIds: util.HashSet[String] = new util.HashSet[String]()
                val eventIds: util.ArrayList[String] = new util.ArrayList[String]()

                var isBrowserProduct: Boolean = false // 是否浏览商品, 默认没有浏览
                // 1. 遍历这个设备上5分钟内的所有事件日志
                breakable {
                    logIt.foreach(log => {
                        eventIds.add(log.eventId)
                        // 2. 记录下领优惠全的所有用户
                        if (log.eventId == "coupon") {
                            uids.add(log.uid) // 领优惠券的用户id
                            itemIds.add(log.itemId) // 用户领券的商品id
                        } else if (log.eventId == "clickItem") { // 如果有浏览商品
                            isBrowserProduct = true
                            break
                        }
                    })
                }
                //2. 组合成元组  (是否预警, 预警信息)
                (
                    !isBrowserProduct && uids.size() >= 3,
                    AlertInfo(mid, uids, itemIds, eventIds, System.currentTimeMillis())
                )
        }
        // 5. 过滤掉不需要报警的信息
        val filteredDStream: DStream[AlertInfo] = checkCouponAlertDStream.filter(_._1).map(_._2)
        checkCouponAlertDStream.print
        filteredDStream.print //打印测试

        // 6. 把预警信息写入到 ES
        // 6.1 对同一个mid, 每分钟最多预警一次. 所以需要做去重处理. 去重使用es来实现去重: 当id相同的时候, 后面的会自动覆盖前面的
        // id: mid_分钟
        val alertInfoWithIdDStream = filteredDStream.map(alertInfo => (alertInfo.mid + "_" + alertInfo.ts / 1000 / 60, alertInfo))

        // 6.2 写入到es中
        alertInfoWithIdDStream.foreachRDD(rdd => {
            rdd.foreachPartition(it => MyESUtil.insertBulk("gmall_coupon_alert", it))
        })

        ssc.start()
        ssc.awaitTermination()

    }
}
