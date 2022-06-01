package com.leaf.gateway.enums;

import com.leaf.common.result.IResultCode;

public enum UserAuthErrorEnum implements IResultCode {

    ACCESS_TOKEN_INVALID(400001, "非法token"),
    ACCESS_TOKEN_EXPIRED(400002, "token已过期"),
    ACCESS_DENY_ERROR(410000, "权限不足");

    private int code;

    private String msg;

    UserAuthErrorEnum(int code, String msg) {
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
