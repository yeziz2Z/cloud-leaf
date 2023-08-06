package com.leaf.admin.dto;

import com.leaf.common.validation.group.Create;
import com.leaf.common.validation.group.Update;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SysUserDTO {

    /**
     * 用户ID
     */
    @Null(message = "创建用户id必须为空", groups = {Create.class})
    @NotNull(message = "用户id不能为空", groups = {Update.class})
    private Long id;

    /**
     * 部门ID
     */
    @NotNull(message = "请选择部门")
    private Long orgId;

    /**
     * 用户账号
     */
    @NotEmpty(message = "用户账号不能为空")
    private String username;

    /**
     * 用户昵称
     */
    @NotNull(message = "用户昵称不能为空")
    private String nickName;

    /**
     * 用户邮箱
     */
    @Email(message = "非法的邮箱格式")
    private String email;

    /**
     * 手机号码
     */
    @Size(min = 11, max = 11, message = "手机号格式错误")
    @Pattern(regexp = "^(((13[0-9])|(14[579])|(15([0-3]|[5-9]))|(16)|(17[0135678])|(18[0-9])|(19[89]))\\d{8})$", message = "手机号格式错误")
    private String mobilePhone;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private String gender;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 密码
     */
    @NotEmpty(message = "用户密码不能为空", groups = {Create.class})
    private String password;

    /**
     * 帐号状态
     */
    private Boolean status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色id
     */
    private List<Long> roleIds;

}
