package com.djmanong.mall.portal;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author DjManong
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
public class MallShopPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallShopPortalApplication.class, args);
    }

}
