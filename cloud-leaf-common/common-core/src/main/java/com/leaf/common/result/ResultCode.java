package com.leaf.common.result;

public enum ResultCode implements IResultCode {
    SUCCESS(200, "ok"),

    ERROR(500, "系统异常"),
    ;

    private int code;

    private String msg;

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
