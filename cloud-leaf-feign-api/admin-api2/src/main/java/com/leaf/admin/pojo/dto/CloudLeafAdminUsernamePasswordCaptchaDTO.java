package com.leaf.admin.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuk
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudLeafAdminUsernamePasswordCaptchaDTO {


    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "用户密码不能为空")
    private String password;

    @NotBlank(message = "uid为空")
    private String uid;

    @NotBlank(message = "验证码码不能为空")
    private String captchaCode;

}
