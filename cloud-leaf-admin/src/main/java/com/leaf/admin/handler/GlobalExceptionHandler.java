package com.leaf.admin.handler;

import com.leaf.common.exception.BusinessException;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(value = RuntimeException.class)
    public Result runtimeExceptionHandler(RuntimeException e) {
        log.error("运行时异常:", e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public Result accessDeniedExceptionHandler(AccessDeniedException e) {
        return Result.fail(403, "没有足够权限");
    }

    @ExceptionHandler(value = BusinessException.class)
    public Result businessExceptionHandler(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        ObjectError error = bindingResult.getAllErrors().stream().findFirst().get();

        String errorMessage = error.getDefaultMessage();
        log.warn("实体参数校验失败:{}", errorMessage);
        return Result.fail(errorMessage);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result illegalArgumentExceptionHandler(IllegalArgumentException e) {

        log.warn("实体参数校验失败:{}", e.getMessage());
        return Result.fail(e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result handleException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String msg = String.format("访问的URL[%s]不支持%s请求", requestURI, e.getMethod());
        log.warn(msg);
        return Result.fail(msg);
    }


}
