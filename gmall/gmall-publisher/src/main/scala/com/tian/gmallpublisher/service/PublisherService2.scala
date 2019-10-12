package com.tian.gmallpublisher.service

/**
 * @author tian
 * @date 2019/10/12 15:45
 * @version 1.0.0
 */
trait PublisherService2 {
    /**
     * 根据需要的聚合字段得到销售明细和聚合结构
     *
     * @param date      要查询的日期
     * @param keyword   要查询关键字
     * @param startPage 开始页面
     * @param size      每页显示多少条记录
     * @param aggField  要聚合的字段
     * @param aggSize   聚合后最多多少条记录
     * @return 1. 总数 2. 聚合结果 3. 明细
     *         {
     *             "total": 100,
     *             "stat" : [
     *                 {
     *                     // 年龄段比例
     *                 },
     *                 {
     *                     // 男女比例
     *                 }
     *             ],
     *             "detail": {
     *                 // 明细
     *             }
     *         }
     */
    def getSaleDetailAndAggResultByAggField(date: String,
                                            keyword: String,
                                            startPage: Int,
                                            size: Int,
                                            aggField: String,
                                            aggSize: Int): Map[String, Any]
}

