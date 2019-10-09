package com.tian.gmallpublisher;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.tian.gmallpublisher.mapper")
public class GmallPublisherApplication {
    public static void main(String[] args) {
        SpringApplication.run(GmallPublisherApplication.class, args);
    }

}
