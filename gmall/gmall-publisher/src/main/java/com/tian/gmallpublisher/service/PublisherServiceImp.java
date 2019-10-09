package com.tian.gmallpublisher.service;

import com.tian.gmallpublisher.mapper.DauMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
