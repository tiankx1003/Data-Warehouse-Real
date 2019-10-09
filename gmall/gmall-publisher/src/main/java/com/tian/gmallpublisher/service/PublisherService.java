package com.tian.gmallpublisher.service;

import java.util.Map;

/**
 * @author tian
 * @version 1.0.0
 * @date 2019/10/9 10:41
 */
public interface PublisherService {
    long getDau(String date);

    Map<String, Long> getHourDau(String date);
}
