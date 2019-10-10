package com.tian.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author tian
 * @version 1.0.0
 * @date 2019/10/9 10:31
 */
public interface DauMapper {
    /**
     * 查询指定日期的日活
     *
     * @param date 指定的日期
     * @return 日活数
     */
    Long getDauTotal(String date);

    /**
     * 小时日活
     *
     * @param date 指定的日期
     * @return 日活数
     */
    List<Map> getDauHour(String date);
}
