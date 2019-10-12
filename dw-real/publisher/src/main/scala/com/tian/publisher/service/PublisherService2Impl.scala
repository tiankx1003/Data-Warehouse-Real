package com.tian.publisher.service

import java.util

import com.tian.common.utils.MyESUtil
import io.searchbox.client.JestClient
import io.searchbox.core.search.aggregation.TermsAggregation
import io.searchbox.core.{Search, SearchResult}

import scala.collection.immutable

/**
 * @author tian
 * @date 2019/10/12 20:59
 * @version 1.0.0
 */
class PublisherService2Impl extends PublisherService2 {
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
                                                     size: Int, aggField: String,
                                                     aggSize: Int): Map[String, Any] = {
        //统计每个年龄段的购买情况
        // TODO: 复习ES，手写DSL语句
        val searchDSL: String =
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
            .addIndex("gmall_sale_detail") // TODO: 建立带分词器的索引报错
            .addType("_doc")
            .build()
        val client: JestClient = MyESUtil.getESClient
        val result: SearchResult = client.execute(search)
        //总数
        val total: Integer = result.getTotal
        //明细
        val detailList: immutable.Seq[Map[String, Any]] = List[Map[String, Any]]()
        val hits: util.List[SearchResult#Hit[util.HashMap[String, Any], Void]] =
            result.getHits(classOf[util.HashMap[String, Any]])
        import scala.collection.JavaConversions._ //使用scala的遍历方式，需要隐式转换
        for (hit <- hits) {
            val source: util.HashMap[String, Any] = hit.source
            detailList.add(source.toMap)
        }
        //聚合
        var aggMap: Map[String, Long] = Map[String, Long]()
        val buckets: util.List[TermsAggregation#Entry] = result.getAggregations.getTermsAggregation(s"groupby_$aggField").getBuckets
        for (bucket <- buckets) {
            aggMap += bucket.getKey -> bucket.getCount
        }
        Map("total" -> total, "aggMap" -> aggMap, "detail" -> detailList)
    }
}
