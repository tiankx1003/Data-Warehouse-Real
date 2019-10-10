package com.tian.dw.gmall.realtime.bean

/**
 * @author tian
 * @date 2019/10/10 14:44
 * @version 1.0.0
 */
case class EventLog(mid: String,
                    uid: String,
                    appId: String,
                    area: String,
                    os: String,
                    logType: String,
                    eventId: String,
                    pageId: String,
                    nextPageId: String,
                    itemId: String,
                    ts: Long,
                    var logDate: String,
                    var logHour: String)

