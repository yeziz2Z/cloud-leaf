package com.leaf.admin.sys.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SysUserDTO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 部门ID
     */
    private Long orgId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
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
