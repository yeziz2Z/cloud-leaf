package com.leaf.oauth2.handler;

import com.leaf.common.result.Result;
import com.leaf.oauth2.enums.AuthorizationErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthExceptionHandler {

    @ExceptionHandler(value = InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result InvalidTokenExceptionHandler(InvalidTokenException e) {
        log.warn("token异常:{}", e.getMessage());
        return Result.result(AuthorizationErrorEnum.REFRESH_TOKEN_EXPIRED);
    }
}
