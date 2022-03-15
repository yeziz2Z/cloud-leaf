package com.leaf.admin.sys.controller;

import com.leaf.admin.sys.dto.UserVO;
import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.service.ISysMenuService;
import com.leaf.admin.sys.service.ISysRoleService;
import com.leaf.admin.sys.service.ISysUserService;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    ISysUserService userService;
    @Autowired
    ISysRoleService roleService;
    @Autowired
    ISysMenuService menuService;

    @GetMapping("/info")
    public Result<UserVO> info(Principal principal) {
        SysUser sysUser = userService.getByUsername(principal.getName());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser, userVO);
        userVO.setRoles(roleService.getRolesByUserId(sysUser.getId()));
        log.info("UserVO ：{}", userVO);
        return Result.success(userVO);
    }


    @GetMapping("/nav")
    public Result nav(Principal principal) {
        SysUser sysUser = userService.getByUsername(principal.getName());
        return Result.success(menuService.selectByUserId(sysUser.getId()));
    }
}
