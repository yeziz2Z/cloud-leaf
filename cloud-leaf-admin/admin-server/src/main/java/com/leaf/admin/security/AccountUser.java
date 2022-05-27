package com.leaf.admin.security;

import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

@Data
public class AccountUser /*implements UserDetails*/ {

    private Long userId;

    private String password;

    private final String username;

    private final Collection/*<? extends GrantedAuthority>*/ authorities;

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;


    public AccountUser(Long userId, String username, String password, Collection/*<? extends GrantedAuthority>*/ authorities) {
        this(userId, username, password, true, true, true, true, authorities);
    }


    public AccountUser(Long userId, String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired, boolean accountNonLocked,
                       Collection/*<? extends GrantedAuthority>*/ authorities) {
        Assert.isTrue(username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }


}
