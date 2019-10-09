package com.tian.dw.gmall.realtime.bean

/**
 *
 * @author tian
 * @version 1.0
 *          2019/10/8 9:07
 */
case class StartupLog(mid: String,
                      uid: String,
                      appId: String,
                      area: String,
                      os: String,
                      channel: String,
                      logType: String,
                      version: String,
                      ts: Long,
                      var logDate: String,
                      var logHour: String)
