package com.leaf.admin.sys.vo;

import com.leaf.admin.sys.entity.SysMenu;
import lombok.Data;
import lombok.ToString;

import java.util.List;

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
