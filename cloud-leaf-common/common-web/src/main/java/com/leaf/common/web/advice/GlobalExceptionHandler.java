package com.leaf.common.web.advice;


import com.leaf.common.exception.BusinessException;
import com.leaf.common.result.Result;
import com.leaf.common.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public Result<Void> runtimeExceptionHandler(RuntimeException e) {
        log.error("运行时异常:", e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result<Void> exceptionHandler(Exception e) {
        log.error("系统异常:", e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    public Result<Void> businessExceptionHandler(BusinessException e) {
        log.warn("自定义业务异常 :{}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Void> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        ObjectError error = bindingResult.getAllErrors().stream().findFirst().get();
        String errorMessage = error.getDefaultMessage();
        log.warn("实体参数校验失败:{}", errorMessage);
        return Result.fail(ResultCode.BAD_REQUEST_ERROR.getCode(), errorMessage);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<Void> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.warn("实体参数校验失败:{}", e.getMessage());
        return Result.fail(ResultCode.BAD_REQUEST_ERROR.getCode(), e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result<Void> handleException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String msg = String.format("访问的URL[%s]不支持%s请求", requestURI, e.getMethod());
        log.warn(msg);
        return Result.fail(msg);
    }

}

