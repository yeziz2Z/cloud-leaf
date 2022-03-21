package com.leaf.admin.sys.dto;

import lombok.Data;

@Data
public class UserQueryParam {

    private Long orgId;

    private String username;

    private String mobilePhone;

    private String status;

}
