package com.leaf.oauth2.enums;

import com.leaf.common.result.IResultCode;

public enum AuthorizationErrorEnum implements IResultCode {

    REFRESH_TOKEN_EXPIRED(400003, "REFRESH_TOKEN已过期!");

    private final int code;

    private final String msg;

    AuthorizationErrorEnum(int code, String msg) {
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
