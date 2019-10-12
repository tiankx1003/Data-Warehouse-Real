package com.tian.dw.gmalldw2es.app

import com.tian.dw.gmalldw2es.bean.SaleDetailDayCount
import com.tian.gmall.common.util.MyESUtil
import org.apache.spark.sql.{Dataset, SparkSession}

/**
 * @author tian
 * @date 2019/10/12 8:58
 * @version 1.0.0
 */
object SaleDetailApp {
    def main(args: Array[String]): Unit = {
        // 获取要查询的日期
        val date = if (args.length > 0) args(0) else "2019-10-12"
        val spark: SparkSession = SparkSession
            .builder()
            .master("local[*]")
            .appName("SaleDetailApp")
            .enableHiveSupport()
            .getOrCreate()
        import spark.implicits._
        val sql =
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
        ds.foreachPartition(it => {
            MyESUtil.insertBulk("gmall_sale_detail", it) //it.toList
        })

        spark.stop()
    }
}
