package com.leaf.common.exception;

import com.leaf.common.result.IResultCode;

public class BusinessException extends RuntimeException {
    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(IResultCode result) {
        new BusinessException(result.getCode(), result.getMsg());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
