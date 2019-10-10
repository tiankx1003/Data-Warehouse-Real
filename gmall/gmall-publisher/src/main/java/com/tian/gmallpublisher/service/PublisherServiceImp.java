package com.tian.gmallpublisher.service;

import com.tian.gmallpublisher.mapper.DauMapper;
import com.tian.gmallpublisher.mapper.OrderMapper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tian
 * @version 1.0.0
 * @date 2019/10/9 10:41
 */
@Service
public class PublisherServiceImp implements PublisherService {
    @Autowired
    public DauMapper dauMapper; //无法new对象，使用自动注入

    @Override
    public long getDau(String date) {
        return dauMapper.getDau(date);
    }

    @Override
    public Map<String, Long> getHourDau(String date) {
        List<Map> list = dauMapper.getHourDau(date);
        HashMap<String, Long> resultMap = new HashMap<>();
        for (Map map : list) {
            String hour = (String) map.get("LOGHOUR");
            Long count = (Long) map.get("COUNT");
            resultMap.put(hour, count);
        }
        return resultMap;
    }



    @Override
    public long getDauTotal(String date) {
        return dauMapper.getDauTotal(date);
    }

    @Override
    public Map getDauHour(String date) {
        List<Map> dauHourList = dauMapper.getDauHour(date);

        Map dauHourMap = new HashedMap();
        for (Map map : dauHourList) {
            String hour = (String)map.get("LOGHOUR");
            Long count = (Long) map.get("COUNT");
            dauHourMap.put(hour, count);
        }

        return dauHourMap;
    }

    // 以下为新增
    @Autowired
    OrderMapper orderMapper;
    @Override
    public double getOrderAmountTotal(String date) {
        return orderMapper.getOrderAmountTotal(date);
    }

    @Override
    public Map getOrderAmountHour(String date) {
        List<Map> orderAmountHour = orderMapper.getOrderAmountHour(date);

        Map<String, BigDecimal> orderHourAmountMap = new HashMap<>();
        for (Map map : orderAmountHour) {
            String hour = (String) map.get("CREATE_HOUR");
            BigDecimal amount = (BigDecimal)map.get("SUM");
            orderHourAmountMap.put(hour, amount);
        }

        return orderHourAmountMap;
    }

}
