package com.tian.dw.gmall.mock.util

import java.util.Date

/**
 * 生成随机日期的工具类
 * @author tian
 * @date 2019-9-30 09:08:25
 * @version 1.0.0
 */
object RandomDate {
    def apply(startDate: Date, stopDate: Date, step: Int) = {
        val randomDate = new RandomDate
        val avgStepTime = (stopDate.getTime - startDate.getTime) / step
        randomDate.maxStepTime = 4 * avgStepTime
        randomDate.lastDateTIme = startDate.getTime
        randomDate
    }
}

class RandomDate {
    // 上次 action 的时间
    var lastDateTIme: Long = _
    // 每次最大的步长时间
    var maxStepTime: Long = _

    /**
     * 得到一个随机时间
     *
     * @return
     */
    def getRandomDate = {
        // 这次操作的相比上次的步长
        val timeStep = RandomNumUtil.randomLong(0, maxStepTime)
        lastDateTIme += timeStep
        new Date(lastDateTIme)
    }
}

