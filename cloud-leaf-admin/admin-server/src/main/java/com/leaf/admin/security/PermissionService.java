package com.leaf.admin.security;

import com.leaf.admin.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * @author liuk
 */
@Service("pms")
public class PermissionService {

    ISysUserService sysUserService;

    @Autowired
    public PermissionService(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    public boolean hasPermission(String permission){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.sysUserService.getUserPermissionsByUsername(username).contains(permission);
    }
}
