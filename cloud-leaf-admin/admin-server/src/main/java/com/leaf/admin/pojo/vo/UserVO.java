package com.leaf.admin.pojo.vo;


import cn.hutool.core.util.DesensitizedUtil;
import com.leaf.admin.entity.SysOrganization;
import com.leaf.admin.entity.SysRole;
import com.leaf.common.annotation.DesensitizedField;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@ToString
public class UserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户类型（00系统用户）
     */
    private String userType;

    /**
     * 用户邮箱
     */
    @DesensitizedField(desensitizedType = DesensitizedUtil.DesensitizedType.EMAIL)
    private String email;

    /**
     * 手机号码
     */
    @DesensitizedField(desensitizedType = DesensitizedUtil.DesensitizedType.MOBILE_PHONE)
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
     * 帐号状态（0正常 1停用）
     */
    private Boolean status;


    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;

    private SysOrganization organization;

    private List<SysRole> roles;

    private Set<String> permissions;

}
