package com.leaf.admin.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleMenuDTO {
    private Long roleId;

    private List<Long> menuIds;
}
