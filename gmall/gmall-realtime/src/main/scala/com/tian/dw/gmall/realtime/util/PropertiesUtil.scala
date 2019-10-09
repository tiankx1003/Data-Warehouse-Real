package com.tian.dw.gmall.realtime.util

import java.io.InputStream
import java.util.Properties

/**
 * 读取配置文件
 *
 * @author tian
 * @version 1.0
 *          2019/10/8 9:01
 */
object PropertiesUtil {
    private val is: InputStream = ClassLoader.getSystemResourceAsStream("config.properties")
    private val properties: Properties = new Properties()
    properties.load(is)

    def getProperty(propertyName: String): String = properties.getProperty(propertyName)

    def main(args: Array[String]): Unit = {
        println(getProperty("kafka.broker.list"))
    }
}

