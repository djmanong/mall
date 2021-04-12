package com.djmanong.mall.pms;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author DjManong
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = "com.djmanong.mall.pms.mapper")
@EnableDubbo
@SpringBootApplication
public class MallPmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallPmsApplication.class, args);
    }

}
