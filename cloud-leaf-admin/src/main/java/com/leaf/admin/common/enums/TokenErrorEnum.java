package com.leaf.admin.common.enums;

public enum TokenErrorEnum {
    INVALID_TOKEN(400001, "非法的token"),
    EXPIRED_TOKEN(400002, "已过期的token");

    private int code;

    private String message;

    TokenErrorEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
