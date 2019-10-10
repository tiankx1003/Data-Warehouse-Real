package com.tian.dw.gmall.realtime.bean

/**
 * @author tian
 * @date 2019/10/10 14:44
 * @version 1.0.0
 */
case class AlertInfo(mid: String,
                     uids: java.util.HashSet[String],
                     itemIds: java.util.HashSet[String],
                     events: java.util.List[String],
                     ts: Long)

