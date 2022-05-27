package com.leaf.admin.security.exception;


public class CaptchaException extends /*AuthenticationException*/ RuntimeException {

    public CaptchaException(String msg) {
        super(msg);
    }
}
