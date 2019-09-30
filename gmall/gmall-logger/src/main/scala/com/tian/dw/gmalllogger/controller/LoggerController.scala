package com.tian.dw.gmalllogger.controller

import com.alibaba.fastjson.{JSON, JSONObject}
import org.slf4j.{Logger, LoggerFactory}
import org.springframework.web.bind.annotation.{PostMapping, RequestParam, RestController}


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
}
