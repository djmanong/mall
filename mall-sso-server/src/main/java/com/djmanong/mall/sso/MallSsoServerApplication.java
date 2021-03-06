package com.djmanong.mall.sso;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author DjManong
 */
@EnableDubbo
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MallSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallSsoServerApplication.class, args);
    }

}
