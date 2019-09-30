package com.tian.dw.gmalllogger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tian.dw.gmall.common.constant.GmallConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


// @Controller
@RestController   // 等价于: @Controller + @ResponseBody
public class LoggerController {
    //    @RequestMapping(value = "/log", method = RequestMethod.POST)
    //    @ResponseBody  //表示返回值是一个 字符串, 而不是 页面名
    @PostMapping("/log")  // 等价于: @RequestMapping(value = "/log", method = RequestMethod.POST)
    public String doLog(@RequestParam("log") String log) {
        // 日志转成 JSONObject
        JSONObject logObj = JSON.parseObject(log);
        // 添加时间戳
        logObj = addTS(logObj);
        // 日志落盘
        saveLog(logObj);
        // 发送到 kafka
        sendToKafka(logObj);
        return "success";

    }

    /**
     * 业务:
     *
     * 1. 给日志添加时间戳 (客户端的时间有可能不准, 所以使用服务器端的时间)
     *
     * 2. 日志落盘
     *
     * 3. 日志发送 kafka
     */

    /**
     * 添加时间戳
     *
     * @param logObj
     * @return
     */
    public JSONObject addTS(JSONObject logObj) {
        logObj.put("ts", System.currentTimeMillis());
        return logObj;
    }

    private final Logger logger = LoggerFactory.getLogger(LoggerController.class);

    /**
     * 日志落盘
     * 使用 log4j
     *
     * @param logObj
     */
    public void saveLog(JSONObject logObj) {
        logger.info(logObj.toJSONString());
    }

    // 使用注入的方式来实例化 KafkaTemplate. Spring boot 会自动完成
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate; // TODO: 2019/9/30 autowired报错

    /**
     * 发送日志到 kafka
     *
     * @param logObj
     */
    private void sendToKafka(JSONObject logObj) {
        String logType = logObj.getString("type");
        String topicName = GmallConstant.TOPIC_STARTUP;

        if ("event".equals(logType)) {
            topicName = GmallConstant.TOPIC_EVENT;
        }
        kafkaTemplate.send(topicName, logObj.toJSONString());
    }


}
