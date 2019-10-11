package com.tian.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author tian
 * @version 1.0.0
 * @date 2019/10/10 10:56
 */
public interface OrderMapper {

    /**
     * 获取订单总的销售额
     *
     * @param date
     * @return
     */
    Double getOrderAmountTotal(String date);

    /**
     * 获取每小时的销售额明细
     *
     * @param date
     * @return
     */
    List<Map> getOrderAmountHour(String date);

}

