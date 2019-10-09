package com.tian.gmallpublisher.mapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    Long getDau(String date);
}
