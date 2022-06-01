package com.leaf.common.pojo.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AuthUser implements Serializable {
    private static final long serialVersionUID = 1222124123775L;

    private Long userId;

    private String username;

    private String password;

    private Long orgId;

    private Boolean enabled;

    private List<String> roles;

}
