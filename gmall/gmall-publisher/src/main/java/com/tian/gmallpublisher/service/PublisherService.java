package com.tian.gmallpublisher.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author tian
 * @version 1.0.0
 * @date 2019/10/9 10:41
 */
public interface PublisherService {
    /*
     查询总数
      */
    long getDauTotal(String date);

    /*
    查询小时明细

    相比数据层, 我们把数据结构做下调整, 更方便使用
     */
    Map<String, Long> getDauHour(String date);

    // 以下为新增

    /**
     * 销售额总数
     *
     * @param date
     * @return
     */
    double getOrderAmountTotal(String date);

    /**
     * 获取销售额小时明细
     *
     * @param date
     * @return
     */
    Map<String, BigDecimal> getOrderAmountHour(String date);
}

