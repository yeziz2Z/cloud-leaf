package com.leaf.admin.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CloudLeafAdminUserBO {

    private Long userId;

    private String username;

    private List<String> authorities;
}
