package com.leaf.oauth2.security.userdetails;

import cn.hutool.core.collection.CollUtil;
import com.leaf.common.pojo.auth.AuthUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class AdminUserDetails implements UserDetails {

    public AdminUserDetails(AuthUser user) {
        this.userId = user.getUserId();
        this.orgId = user.getOrgId();
        this.enabled = user.getEnabled();
        this.username = user.getUsername();
        this.password = user.getPassword();
        if (CollUtil.isNotEmpty(user.getRoles())) {
            this.authorities = new ArrayList<>();
            user.getRoles().forEach(role -> this.authorities.add(new SimpleGrantedAuthority(role)));
        }
    }

    private Long userId;

    private Long orgId;

    private String username;

    private String password;

    private Boolean enabled;

    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
