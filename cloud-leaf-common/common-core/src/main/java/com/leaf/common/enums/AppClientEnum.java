package com.leaf.common.enums;

/**
 * @author liuk
 */
public enum AppClientEnum {

    ADMIN("后台管理", "cloud-leaf-admin"),
    BLOG("博客系统", "cloud-leaf-blog");

    private final String name;

    private final String clientCode;

    AppClientEnum(String name, String clientCode) {
        this.name = name;
        this.clientCode = clientCode;
    }

    public String getName() {
        return name;
    }

    public String getClientCode() {
        return clientCode;
    }
}
