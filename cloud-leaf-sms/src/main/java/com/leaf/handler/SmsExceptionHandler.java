package com.leaf.handler;

import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class SmsExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result handlerException(RuntimeException re) {
        log.info(re.getMessage(), re);
        return new Result(500, "系统运行时异常,请联系管理员!");
    }

    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        log.info(e.getMessage(), e);
        return new Result(500, "系统异常");
    }
}
