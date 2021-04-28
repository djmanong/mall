package com.djmanong.mall.portal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 获取线程池信息
 * @author DjManong
 */
@RestController
@Api(tags = "ThreadPoolController", description = "线程池控制器")
public class ThreadPoolController {

    @Qualifier("mainThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor mainThreadPoolExecutor;

    @Qualifier("otherThreadPoolExecutor")
    @Autowired
    ThreadPoolExecutor otherThreadPoolExecutor;

    @GetMapping("/thread/status")
    @ApiOperation(value = "线程池参数信息")
    public Map<String, Object> threadPoolExecutorStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("activeCount", mainThreadPoolExecutor.getActiveCount());
        map.put("核心线程线程池大小", mainThreadPoolExecutor.getCorePoolSize());
        return map;
    }

    @GetMapping("/thread/shutdown")
    @ApiOperation(value = "当系统资源不足时释放非核心业务线程池")
    public Object shutdownThreadPoolExecutor() {
        otherThreadPoolExecutor.shutdown();
        return "ok";
    }
}
