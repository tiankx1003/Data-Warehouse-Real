package com.tian.gmallpublisher.service

import com.tian.gmall.common.util.MyESUtil
import io.searchbox.client.JestClient
import io.searchbox.core.{Search, SearchResult}
import org.springframework.stereotype.Service
import java.util
/**
 * @author tian
 * @date 2019/10/12 15:48
 * @version 1.0.0
 */
@Service
class PublisherServiceImpl2 extends PublisherService2 {
    /**
     * 根据需要的聚合字段得到销售明细和聚合结构
     *
     * @param date      要查询的日期
     * @param keyword   要查询关键字
     * @param startPage 开始页面
     * @param size      每页显示多少条记录
     * @param aggField  要聚合的字段
     * @param aggSize   聚合后最多多少条记录
     * @return 1. 总数 2. 聚合结果 3. 明细
     *         {
     *         "total": 100,
     *         "stat" : [
     *         {
     *         // 年龄段比例
     *         },
     *         {
     *         // 男女比例
     *         }
     *         ],
     *         "detail": {
     *         // 明细
     *         }
     *         }
     */
    override def getSaleDetailAndAggResultByAggField(date: String,
                                                     keyword: String,
                                                     startPage: Int,
                                                     size: Int,
                                                     aggField: String,
                                                     aggSize: Int): Map[String, Any] = {

        // 统计每个年龄购买情况
        val searchDSL =
            s"""
               |{
               |  "from": ${(startPage - 1) * size},
               |  "size": $size,
               |  "query": {
               |    "bool": {
               |      "filter": {
               |        "term": {
               |          "dt": "$date"
               |        }
               |      }
               |      , "must": [
               |        {"match": {
               |          "sku_name": {
               |            "query": "$keyword",
               |            "operator": "and"
               |          }
               |        }}
               |      ]
               |    }
               |  }
               |  , "aggs": {
               |    "groupby_$aggField": {
               |      "terms": {
               |        "field": "user_$aggField",
               |        "size": $aggSize
               |      }
               |    }
               |  }
               |}
         """.stripMargin

        val search: Search = new Search.Builder(searchDSL)
            .addIndex("gmall_sale_detail")
            .addType("_doc")
            .build()

        val client: JestClient = MyESUtil.getESClient
        val result: SearchResult = client.execute(search)

        // 1. 得到总数
        val total: Integer = result.getTotal
        // 2. 得到明细 (scala 集合)
        val detailList = List[Map[String, Any]]() // 存储明细
        val hits: util.List[SearchResult#Hit[util.HashMap[String, Any], Void]] = result.getHits(classOf[util.HashMap[String, Any]])
        import scala.collection.JavaConversions._  // 要是使用 scala 的遍历凡是, 需要隐式转换
        for (hit <- hits) {
            val source: util.HashMap[String, Any] = hit.source
            detailList.add(source.toMap)
        }
        // 3. 得到聚合结果
        var aggMap = Map[String, Long]() // 存储聚合结果
        val buckets = result.getAggregations.getTermsAggregation(s"groupby_$aggField").getBuckets
        for (bucket <- buckets) {
            aggMap += bucket.getKey -> bucket.getCount()
        }

        // 返回最终结果
        Map("total" -> total, "aggMap" -> aggMap, "detail" -> detailList)

    }
}
