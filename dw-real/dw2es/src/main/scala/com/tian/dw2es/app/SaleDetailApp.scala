package com.tian.dw2es.app

import com.tian.common.utils.MyESUtil
import com.tian.dw2es.bean.SaleDetailDayCount
import org.apache.spark.sql.{Dataset, SparkSession}

/**
 * 使用SparkSQL读取Hive中的数据并写入到ES
 *
 * @author tian
 * @date 2019/10/12 19:49
 * @version 1.0.0
 */
object SaleDetailApp {
    def main(args: Array[String]): Unit = {
        //获取需要查询的日期
        val date: String = if (args.length > 0) args(0) else "2019-10-12"
        val spark: SparkSession = SparkSession
            .builder()
            .master("local[2]")
            .appName("SaleDetailApp")
            .enableHiveSupport() //开启Hive支持
            .getOrCreate()
        import spark.implicits._
        val sql: String =
            s"""
               |select
               |    user_id,
               |    sku_id,
               |    user_gender,
               |    cast(user_age as int) user_age,
               |    user_level,
               |    cast(order_price as double) order_price,
               |    sku_name,
               |    sku_tm_id,
               |    sku_category3_id,
               |    sku_category2_id,
               |    sku_category1_id,
               |    sku_category3_name,
               |    sku_category2_name,
               |    sku_category1_name,
               |    spu_id,
               |    sku_num,
               |    cast(order_count as bigint) order_count,
               |    cast(order_amount as double) order_amount,
               |    dt
               |from dws_sale_detail_daycount
               |where dt='$date'
             """.stripMargin
        spark.sql("use gmall")
        val ds: Dataset[SaleDetailDayCount] = spark.sql(sql).as[SaleDetailDayCount]
        //数据写入到ES
        ds.foreachPartition(it => MyESUtil.insertBulk("gmall_sale_detail", it)) //it.toList
        spark.stop()
    }
}
