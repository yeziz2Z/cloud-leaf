package com.leaf.admin.sys.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserQueryParam {

    private Long orgId;

    private String name;

    private String phone;

    private String status;

}
