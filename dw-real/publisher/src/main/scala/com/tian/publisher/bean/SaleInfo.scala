package com.tian.publisher.bean

/**
 * 封装返回给前端的所有数据
 *
 * @author tian
 * @date 2019/10/12 21:19
 * @version 1.0.0
 */
case class SaleInfo(total: Int,
                    stats: List[Stat],
                    detail: List[Map[String, Any]])
