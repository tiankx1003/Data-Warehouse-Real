package com.tian.dw.gmall.realtime.app

import java.text.SimpleDateFormat
import java.util.Date

import com.alibaba.fastjson.JSON
import com.tian.dw.gmall.realtime.bean.OrderInfo
import com.tian.gmall.common.ConstantUtil
import com.tian.dw.gmall.realtime.util.MyKafkaUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author tian
 * @date 2019-10-10 10:18:47
 * @version 1.0
 */
object OrderApp {
    def main(args: Array[String]): Unit = {

        //1 . 从kafka消费数据
        val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("OrderApp")
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(2))
        val sourceDStream: InputDStream[(String, String)] = MyKafkaUtil.getKafkaStream(ssc, ConstantUtil.TOPIC_ORDER)
        val orderInfoDStream: DStream[OrderInfo] = sourceDStream.map { // 对数据格式做调整
            case (_, value) => {
                //对某些字段进行脱敏处理
                val orderInfo: OrderInfo = JSON.parseObject(value, classOf[OrderInfo]) // 李小名 => 李**
                orderInfo.consignee = orderInfo.consignee.substring(0, 1) + "**" // 李小名 => 李**
                orderInfo.consignee_tel = orderInfo.consignee_tel.substring(0, 3) +
                    "****" + orderInfo.consignee_tel.substring(7, 11)
                // orderInfo.consignee_tel.replaceAll("\\d{3}\\d{4}\\d{4}","$1****$2") //使用正则替换字符


                // 计算 createDate 和 createHour
                val date: Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(orderInfo.create_time)
                orderInfo.create_date = new SimpleDateFormat("yyyy-MM-dd").format(date)
                orderInfo.create_hour = new SimpleDateFormat("HH").format(date)
                orderInfo
            }
        }

        //2. 把数据写入到 Phoenix
        import org.apache.phoenix.spark._
        orderInfoDStream.foreachRDD(rdd => {
            rdd.saveToPhoenix(
                ConstantUtil.ORDER_TABLE_NAME, //传入表名参数时要全大写
                Seq(
                    "ID", "PROVINCE_ID", "CONSIGNEE", "ORDER_COMMENT", "CONSIGNEE_TEL",
                    "ORDER_STATUS", "PAYMENT_WAY", "USER_ID", "IMG_URL", "TOTAL_AMOUNT",
                    "EXPIRE_TIME", "DELIVERY_ADDRESS", "CREATE_TIME", "OPERATE_TIME",
                    "TRACKING_NO", "PARENT_ORDER_ID", "OUT_TRADE_NO", "TRADE_BODY",
                    "CREATE_DATE", "CREATE_HOUR"
                ),
                zkUrl = Some("hadoop102,hadoop103,hadoop104:2181"))
        })
        orderInfoDStream.print
        ssc.start()
        ssc.awaitTermination()

    }

}

