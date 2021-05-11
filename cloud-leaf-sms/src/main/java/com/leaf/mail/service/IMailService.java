package com.leaf.mail.service;

public interface IMailService {

    /**
     *
     * @param to 收件人
     * @param code 验证码
     */
    void send(String to, String code);
}
