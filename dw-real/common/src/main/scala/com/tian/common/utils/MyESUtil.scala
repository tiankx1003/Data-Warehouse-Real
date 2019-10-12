package com.tian.common.utils

import com.tian.common.bean.User
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.core.{Bulk, Index}

/**
 * ES工具类
 *
 * @author tian
 * @date 2019/10/12 19:55
 * @version 1.0.0
 */
object MyESUtil {
    val esUrl: String = "http://hadoop102:9092"
    val factory: JestClientFactory = new JestClientFactory
    val conf: HttpClientConfig = new HttpClientConfig.Builder(esUrl)
        .multiThreaded(true)
        .maxTotalConnection(20)
        .connTimeout(10000)
        .readTimeout(10000)
        .build()
    factory.setHttpClientConfig(conf)

    /**
     * @return ElasticSearch客户端
     */
    def getESClient: JestClient = factory.getObject

    /**
     * 插入单条数据
     *
     * @param indexName ES索引名(表名)
     * @param source    插入内容
     */
    def insertSingle(indexName: String, source: Any): Unit = { // TODO: 2019-10-12 20:03:10 确定source参数的含义
        val client: JestClient = getESClient
        val index: Index = new Index.Builder(source)
            .`type`("_doc")
            .index(indexName)
            .build()
        client.execute(index)
        client.close()
    }

    /**
     * 插入多条数据
     *
     * @param indexName ES索引名(表名)
     * @param sources   插入的内容
     */
    def insertBulk(indexName: String, sources: Iterator[Any]): Unit = {
        if (sources.isEmpty) return //如果插入内容为空直接返回
        val client: JestClient = getESClient
        val bulkBuilder: Bulk.Builder = new Bulk.Builder()
            .defaultIndex(indexName) //TODO: 2019-10-12 20:08:20 设置index和type还有别的写法
            .defaultType("_doc")
        sources.foreach { //遍历所有的source转成action，并添加在所有的bulk中
            //传入的数据为元祖，第一个元素为id
            case (id: String, data) => bulkBuilder.addAction(new Index.Builder(data).id(id).build())
            //若数据类型不是元祖则没有id，自动生成id
            case data => bulkBuilder.addAction(new Index.Builder(data).build())
        }
        client.execute(bulkBuilder.build())
        client.close()
    }

    /**
     * 关闭连接
     *
     * @param client ES客户端
     */
    def closeClient(client: JestClient): Unit = {
        if (client != null) {
            try {
                closeClient(client)
            } catch {
                case e: Throwable => e.printStackTrace()
            }
        }
    }

    def main(args: Array[String]): Unit = { // TODO: 测试插入数据
        insertSingle("user", User("a", 20))
        insertBulk("user", Iterator(User("aa", 20), User("bb", 30)))
    }
}
