package com.leaf.common.enums;

public enum YesOrNoEnum {
    YES("1", "是"),
    NO("0", "否");


    private final String code;

    private final String desc;

    YesOrNoEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
