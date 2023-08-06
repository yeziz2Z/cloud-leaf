package com.leaf.admin.dto;

import lombok.Data;

@Data
public class UserLoginDTO {

    private String username;

    private String password;

    private String uid;

    private String captcha;

    private String rememberMe;
}
