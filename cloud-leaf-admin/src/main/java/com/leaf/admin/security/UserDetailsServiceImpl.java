package com.leaf.admin.security;

import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ISysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.getByUsername(username);

        if (Objects.isNull(sysUser)) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }

        return new AccountUser(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword(), getUserAuthorities(sysUser.getId()));
    }

    public List<GrantedAuthority> getUserAuthorities(Long userId) {
//        return AuthorityUtils.commaSeparatedStringToAuthorityList(sysUserService.getUserAuthorities(userId));
        return null;
    }

    public List<GrantedAuthority> getUserAuthoritiesByUsername(String username) {
        /*SysUser sysUser = sysUserService.getByUsername(username);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(sysUserService.getUserAuthorities(sysUser.getId()));*/
        return null;
    }
}