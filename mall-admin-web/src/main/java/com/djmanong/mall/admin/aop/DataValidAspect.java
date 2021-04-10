package com.djmanong.mall.admin.aop;

import com.djmanong.mall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * 数据验证切面 利用AOP完成统一的数据校验，数据校验出错就返回给前端错误提示
 *      前置通知:  方法执行之前触发
 *      后置通知： 方法执行之后触发
 *      返回通知： 方法正常返回之后触发
 *      异常通知： 方法异常返回之后触发
 *
 * 1. 正常执行：前置通知 - 返回通知 - 后置通知
 * 2. 异常执行：前置通知 - 异常通知 - 后置通知
 *
 *      环绕通知：4合1
 *
 * @author DjManong
 */
@Aspect
@Component
@Slf4j
public class DataValidAspect {

    /**
     * 环绕数据校验
     * 目标方法的异常，一般都需要再次抛出
     * @param point
     * @return
     */
    @Around("execution(* com.djmanong.mall.admin..*Controller.*(..))")
    public Object validAround(ProceedingJoinPoint point) {

        Object proceed = null;
        try {
            log.debug("数据校验切面介入工作...");
            // 前置

            Object[] args = point.getArgs();
            for (Object arg : args) {
                if (arg instanceof BindingResult) {
                    BindingResult result = (BindingResult) arg;
                    if (result.getErrorCount() > 0) {
                        // 校验到参数错误
                        return new CommonResult().validateFailed(result);
                    }
                }
            }

            // point.proceed() 就是 method.invoke()
            proceed = point.proceed(point.getArgs());
            log.debug("切面放行目标方法...");
            // 返回
        } catch (Throwable throwable) {
            // 异常

            // 异常不能直接处理，否则传递不到全局异常处理器
            throw new RuntimeException(throwable);
        } finally {
            // 后置
        }
        return proceed;
    }
}
