package com.leaf.admin.common.enums;

public enum LoginEnum {
    CAPTCHA_ERROR(500, "验证码错误"),
    USERNAME_PASSWORD_ERROR(500, "用户名或密码错误"),
    ;


    private int code;

    private String message;

    LoginEnum(int code, String message) {
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
