package com.leaf.admin.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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


    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "用户密码不能为空")
    private String password;

    @NotNull(message = "uid为空")
    private String uid;

    @NotNull(message = "验证码码不能为空")
    private String captchaCode;

}
