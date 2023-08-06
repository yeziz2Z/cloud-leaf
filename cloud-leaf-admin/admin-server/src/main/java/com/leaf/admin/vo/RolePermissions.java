package com.leaf.admin.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuk
 */
@Data
@ToString
public class RolePermissions {

    /**
     * 角色编码
     */
    private String code;

    /**
     * 接口 url
     */
    private String permissionUrl;

}
