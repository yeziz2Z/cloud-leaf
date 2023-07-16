package com.leaf.oauth2.security.userdetails.impl;

import com.leaf.admin.api.UserFeignClient;
import com.leaf.common.pojo.auth.AuthUser;
import com.leaf.common.result.Result;
import com.leaf.oauth2.security.userdetails.AdminUserDetails;
import com.leaf.oauth2.security.userdetails.OAuth2ClientUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author liuk
 */
@Component
public class AdminUserDetailsServiceImpl implements OAuth2ClientUserDetailsService {

    private final UserFeignClient userFeignClient;

    @Autowired
    public AdminUserDetailsServiceImpl(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    private final static String CLIENT_ID = "cloud-leaf-admin";

    @Override
    public boolean support(String clientId) {
        return CLIENT_ID.equals(clientId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUserDetails userDetails = null;
        Result<AuthUser> result = userFeignClient.loadByUsername(username);
        if (result.getCode() == Result.success().getCode()) {
            AuthUser authUser = result.getData();
            userDetails =  new AdminUserDetails(authUser);
        }
        if (userDetails == null) {
            throw new UsernameNotFoundException("用户不存在");
        } else if (!userDetails.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return userDetails;
    }
}
