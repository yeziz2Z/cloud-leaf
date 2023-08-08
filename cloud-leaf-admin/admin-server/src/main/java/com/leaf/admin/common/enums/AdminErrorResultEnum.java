package com.leaf.admin.common.enums;

import com.leaf.common.result.IResultCode;

public enum AdminErrorResultEnum implements IResultCode {

    USER_CODE_EXISTS(10001,"用户已存在");

    private final int code;

    private final String msg;


    AdminErrorResultEnum(int code, String msg) {
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
