package com.djmanong.mall.portal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 配置系统线程池信息
 * @author DjManong
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 核心业务线程池
     * @param poolProperties
     * @return
     */
    @Bean("mainThreadPoolExecutor")
    public ThreadPoolExecutor mainThreadPoolExecutor(@Autowired PoolProperties poolProperties) {
        LinkedBlockingDeque<Runnable> mainQueue = new LinkedBlockingDeque<>(poolProperties.getQueueSize());
        return new ThreadPoolExecutor(poolProperties.getCorePoolSize(),
                poolProperties.getMaximumPoolSize(),
                10,
                TimeUnit.MINUTES,
                mainQueue);
    }

    /**
     * 非核心业务线程池
     * @param poolProperties
     * @return
     */
    @Bean("otherThreadPoolExecutor")
    public ThreadPoolExecutor otherThreadPoolExecutor(@Autowired PoolProperties poolProperties) {
        LinkedBlockingDeque<Runnable> otherQueue = new LinkedBlockingDeque<>(poolProperties.getQueueSize());
        return new ThreadPoolExecutor(poolProperties.getCorePoolSize(),
                poolProperties.getMaximumPoolSize(),
                10,
                TimeUnit.MINUTES,
                otherQueue);
    }
}
