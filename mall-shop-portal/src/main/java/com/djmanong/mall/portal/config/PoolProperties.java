package com.djmanong.mall.portal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池参数配置
 * @author DjManong
 */
@Data
@Component
@ConfigurationProperties(prefix = "mall.pool")
public class PoolProperties {
    /**核心线程数*/
    private Integer corePoolSize;

    /**最大线程数*/
    private Integer maximumPoolSize;

    /**阻塞队列长度*/
    private Integer queueSize;
}
