package com.leaf.common.enums;

public enum BusinessTypeEnum {

    OTHER(0, "其他"),
    INSERT(1, "新增"),
    UPDATE(2, "修改"),
    SELECT(3, "查询"),
    DELETE(4, "删除"),
    GRANT(5, "授权"),
    IMPORT(6, "导入"),
    EXPORT(7, "导出");

    private Integer code;

    private String desc;

    BusinessTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
