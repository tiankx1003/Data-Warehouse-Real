package com.tian.dw.gmalllogger.controller

import com.alibaba.fastjson.{JSON, JSONObject}
import com.tian.gmall.common.ConstantUtil
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.web.bind.annotation.{PostMapping, RequestParam, RestController}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate

/**
 * @author tian
 *         date 2019/9/30 10:42
 */
//@Controller + @ResponseBody = @RestController
@RestController
class LoggerController {
    // http://localhost:8080/log
    @PostMapping(Array("/log"))
    def doLog(@RequestParam("log") log: String): String = {
        println(log)
        val logObj = JSON.parseObject(log)
        val logWithTS = addTS(logObj)
        saveLog2File(logWithTS)
        send2Kafka(logWithTS)
        "success"
    }

    /**
     * 添加时间戳
     *
     * @param logObj
     * @return
     */
    def addTS(logObj: JSONObject): String = {
        logObj.put("ts", System.currentTimeMillis)
        logObj.toJSONString
    }

    private val logger: Logger = LoggerFactory.getLogger(classOf[LoggerController])

    /**
     * 数据落盘
     *
     * @param logWithTS
     */
    def saveLog2File(logWithTS: String) = {
        logger.info(logWithTS)
    }


    // 使用注入的方式来实例化 KafkaTemplate. Spring boot 会自动完成// 使用注入的方式来实例化 KafkaTemplate. Spring boot 会自动完成
    @Autowired var kafkaTemplate: KafkaTemplate[String, String] = _

    /**
     * 发送日志到 kafka
     *
     * @param logWithTS
     */
    private def send2Kafka(logWithTS: String): Unit = {
        val logObj = JSON.parseObject(logWithTS)
        val logType = logObj.getString("logType")
        var topicName = ConstantUtil.STARTUP_TOPIC
        if ("event" == logType) topicName = ConstantUtil.EVENT_TOPIC
        kafkaTemplate.send(topicName, logObj.toJSONString)
    }
}
