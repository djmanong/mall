package com.djmanong.mall.admin;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author DjManong
 *
 * @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}): 不进行数据源的自动配置
 *
 * 如果导入的依赖，引入一个自动配置的场景：
 *  1. 这个场景自动配置默认生效，就必须配置它
 *  2. 不想配置：
 *      1) 排除依赖
 *      2) 排除这个场景的自动配置类
 */
@EnableDubbo
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MallAdminWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallAdminWebApplication.class, args);
    }

}
