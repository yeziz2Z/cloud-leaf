package com.leaf.common.result;

public enum ResultCode implements IResultCode {
    SUCCESS(200, "ok"),

    ERROR(500, "系统异常"),
    INVALID_TOKEN(23001, "token无效或已过期"),
    TOKEN_ACCESS_FORBIDDEN(23002, "token已被禁止访问"),

    AUTHORIZED_ERROR(24001, "访问权限异常"),
    ACCESS_UNAUTHORIZED(24002, "访问未授权");


    private final int code;

    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
