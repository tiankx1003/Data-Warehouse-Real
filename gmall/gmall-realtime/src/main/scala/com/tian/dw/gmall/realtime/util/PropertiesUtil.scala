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
    private val properties = new Properties()
    properties.load(is)

    def getProperty(propertyName: String): String = properties.getProperty(propertyName)

    //TODO 另一种写法
    def getProperty(propFile:String,propName:String) = {

    }

    def main(args: Array[String]): Unit = {
        println(getProperty("kafka.broker.list"))
    }
}

