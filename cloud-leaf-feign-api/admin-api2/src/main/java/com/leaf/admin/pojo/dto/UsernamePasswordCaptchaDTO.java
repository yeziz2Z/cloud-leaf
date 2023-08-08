package com.leaf.admin.pojo.dto;

import lombok.Data;

/**
 * @author liuk
 */
@Data
public class UsernamePasswordCaptchaDTO {

    private String username;

    private String password;

    private String uid;

    private String captchaCode;

}
