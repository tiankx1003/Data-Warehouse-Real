package com.tian.dw.gmall.realtime.util

import com.tian.dw.gmall.realtime.util.MyESUtil.closeClient
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.client.config.HttpClientConfig
import io.searchbox.core.Index

/**
 * 连接ES并写入数据Demo
 *
 * @author tian
 * @date 2019/10/11 15:26
 * @version 1.0.0
 */
object ESDemo {
    def main(args: Array[String]): Unit = {
        val esUrl: String = "http://hadoop102:9200"
        val factory: JestClientFactory = new JestClientFactory
        val conf: HttpClientConfig = new HttpClientConfig.Builder(esUrl)
            .multiThreaded(true)
            .maxTotalConnection(20)
            .connTimeout(10000)
            .readTimeout(10000)
            .build()
        factory.setHttpClientConfig(conf)
        val client: JestClient = factory.getObject
        val source1: String = ""
        val source2: People = new People("Tom", 19)
        val index: Index = new Index
        .Builder(source1)
            .`type`("_doc")
            .index("ESDemo")
            .build()
        client.execute(index)
        closeClient(client)
    }
}

case class People(name: String, age: Int)