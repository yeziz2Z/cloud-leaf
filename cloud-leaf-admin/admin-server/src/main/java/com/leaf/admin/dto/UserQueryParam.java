package com.leaf.admin.dto;

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
