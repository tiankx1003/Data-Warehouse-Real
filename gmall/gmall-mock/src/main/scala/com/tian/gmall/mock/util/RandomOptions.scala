package com.tian.gmall.mock.util

import scala.collection.mutable.ListBuffer

/**
 * 按照一定的分布生成随机选项的工具类
 * 根据提供的值和比重, 来创建RandomOptions对象.
 * 然后可以通过getRandomOption来获取一个随机的预定义的值
 *
 * @author tian
 * @date 2019-9-30 09:07:35
 * @version 1.0.0
 */
object RandomOptions {
    def apply[T](opts: (T, Int)*) = {
        val randomOptions = new RandomOptions[T]()
        randomOptions.totalWeight = (0 /: opts) (_ + _._2) // 计算出来总的比重
        opts.foreach {
            case (value, weight) => randomOptions.options ++= (1 to weight).map(_ => value)
        }
        randomOptions
    }
}

class RandomOptions[T] {
    var totalWeight: Int = _
    var options = ListBuffer[T]()

    /**
     * 获取随机的 Option 的值
     *
     * @return
     */
    def getRandomOption() = {
        options(RandomNumUtil.randomInt(0, totalWeight - 1))
    }
}

