package com.leaf.admin.pojo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author liuk
 */
@Data
public class ResetPasswordDTO {

    @NotNull(message = "用户Id不能为空")
    private Long userId;

    @NotNull(message = "密码不能为空")
    @Length(min = 6, max = 16, message = "密码长度为6至16位")
    private String password;
}
