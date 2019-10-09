package com.tian.gmallpublisher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.tian.gmallpublisher.mapper")
public class GmallPublisherApplication {
    // TODO: 2019/10/9 数据处理接口主程序运行有问题
    public static void main(String[] args) {
        SpringApplication.run(GmallPublisherApplication.class, args);
    }

}
