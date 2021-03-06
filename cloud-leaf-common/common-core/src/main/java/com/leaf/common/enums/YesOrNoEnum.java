package com.leaf.common.enums;

public enum YesOrNoEnum {
    YES(1, "是"),
    NO(0, "否");


    private Integer code;

    private String desc;

    YesOrNoEnum(Integer code, String desc) {
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
