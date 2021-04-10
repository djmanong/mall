package com.djmanong.mall.admin.aop;

import com.djmanong.mall.to.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一处理所有异常，给前端返回500的json
 * @RestControllerAdvice注解： 标注异常处理类并返回json数据 @RestController + @ControllerAdvice
 *
 * 当编写环绕通知的时候，目标异常一定要再次抛出
 *
 * @author DjManong
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ArithmeticException.class})
    public Object handlerException(Exception exception) {
        log.error("系统出现异常, 信息: {}", (Object) exception.getStackTrace());
        return new CommonResult().validateFailed("数学运算异常");
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public Object handlerException02(Exception exception) {
        log.error("系统出现异常, 信息: {}", (Object) exception.getStackTrace());
        return new CommonResult().validateFailed("空指针异常");
    }
}
