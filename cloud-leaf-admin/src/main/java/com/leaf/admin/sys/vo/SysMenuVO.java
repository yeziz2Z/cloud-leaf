package com.leaf.admin.sys.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SysMenuVO {
    private Long id;

    /**
     * 菜单编码名
     */
    private String name;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 父级菜单id
     */
    private Long parentId;

    /**
     * 组件名
     */
    private String component;

    /**
     * 请求路由
     */
    private String path;

    /**
     * 控制路由和子路由是否显示在 sidebar
     */
    private Boolean hidden;

    /**
     * 重定向地址, 访问这个路由时,自定进行重定向
     */
    private String redirect;

    /**
     * 强制菜单显示为Item而不是SubItem(配合 meta.hidden)
     */
    private Boolean hideChildrenInMenu;

    /**
     * 菜单类型 F目录 M菜单 B按钮
     */
    private String type;

    /**
     * 排序
     */
    private Integer orderNo;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * *特殊 隐藏 PageHeader 组件中的页面带的 面包屑和页面标题栏
     */
    private Boolean hiddenHeaderContent;

    /**
     * 缓存该路由 (开启 multi-tab 是默认值为 true)
     */
    private Boolean keepAlive;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 新窗口打开
     */
    private Boolean newWindow;

    private Boolean status;

    /**
     * 创建人
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

    private List<SysMenuVO> children;

}
