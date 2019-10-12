package com.tian.gmall.common.util

import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.core.{Bulk, Index}

/**
 * @author tian
 * @date 2019/10/11 15:08
 * @version 1.0.0
 */
object MyESUtil {
    val esUrl: String = "http://hadoop102:9200"
    val factory: JestClientFactory = new JestClientFactory
    val conf: HttpClientConfig = new HttpClientConfig.Builder(esUrl)
        .multiThreaded(true)
        .maxTotalConnection(20)
        .connTimeout(10000)
        .readTimeout(10000)
        .build()
    factory.setHttpClientConfig(conf)

    // 获取客户端
    def getESClient: JestClient = factory.getObject

    // 插入单条数据
    def insertSingle(indexName: String, source: Any): Unit = {
        val client: JestClient = getESClient
        val index: Index = new Index.Builder(source)
            .`type`("_doc")
            .index(indexName)
            .build()
        client.execute(index)
        closeClient(client)
    }

    // 插入多条数据 sources:   Iterable[(id, caseClass)] 或者 Iterable[caseClass]
    def insertBulk(indexName: String, sources: Iterator[Any]): Unit = {
        if (sources.isEmpty) return

        val client: JestClient = getESClient
        val bulkBuilder: Bulk.Builder = new Bulk.Builder()
            .defaultIndex(indexName)
            .defaultType("_doc")
        sources.foreach { // 把所有的source变成action添加buck中
            //传入的是值是元组, 第一个表示id
            case (id: String, data) => bulkBuilder.addAction(new Index.Builder(data).id(id).build())
            // 其他类型 没有id, 将来省的数据会自动生成默认id
            case data => bulkBuilder.addAction(new Index.Builder(data).build())
        }
        // val bulk: Bulk = new Bulk.Builder().build
        // client.execute(bulk)
        client.execute(bulkBuilder.build())
        closeClient(client)
    }

    //关闭客户端
    def closeClient(client: JestClient): Unit = {
        if (client != null) {
            try {
                client.shutdownClient()
            } catch {
                case e: Throwable => e.printStackTrace()
            }
        }
    }

    def main(args: Array[String]): Unit = { //运行测试效果
        //        insertSingle("user", User("a", 20))
        insertBulk("user", Iterator(User("aa", 20), User("bb", 30)))

    }
}
case class User(name: String, age: Int)
