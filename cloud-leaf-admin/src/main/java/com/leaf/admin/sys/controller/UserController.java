package com.leaf.admin.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.sys.dto.SysUserDTO;
import com.leaf.admin.sys.dto.UserQueryParam;
import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.service.ISysMenuService;
import com.leaf.admin.sys.service.ISysOrganizationService;
import com.leaf.admin.sys.service.ISysRoleService;
import com.leaf.admin.sys.service.ISysUserService;
import com.leaf.admin.sys.vo.UserVO;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    @Autowired
    ISysOrganizationService organizationService;

    @GetMapping("/info")
    public Result<UserVO> info(Principal principal) {
        SysUser sysUser = userService.getByUsername(principal.getName());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser, userVO);
        userVO.setRoles(roleService.getRolesByUserId(sysUser.getId()));
        userVO.setPermissions(menuService.selectUserPermissions(sysUser.getId()));
        log.info("UserVO ：{}", userVO);
        return Result.success(userVO);
    }


    @GetMapping("/nav")
    public Result nav() {
        return Result.success(userService.getCurrentUserNav());
    }

    @GetMapping("/service")
    public Result list(Page page, UserQueryParam queryParam) {
        log.info("queryParam {}, Page {}", queryParam, page);
        return Result.success(userService.selectSysUserVOPage(page, queryParam));
    }

    @PostMapping
    public Result add(@RequestBody SysUserDTO sysUser) {
        userService.saveUser(sysUser);
        return Result.success();
    }

    @PutMapping
    public Result edit(@RequestBody SysUserDTO sysUser) {
        userService.updateUser(sysUser);
        return Result.success();
    }

    @PutMapping("/resetPassword")
    public Result resetPassword(@RequestBody SysUserDTO sysUser) {

        return Result.success();
    }

    @GetMapping("/{userId}")
    public Result<UserVO> getUserById(@PathVariable("userId") Long userId) {
        SysUser sysUser = userService.getById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser, userVO);
        userVO.setRoles(roleService.getRolesByUserId(sysUser.getId()));
        userVO.setOrganization(organizationService.getById(sysUser.getOrgId()));
        return Result.success(userVO);
    }

    @DeleteMapping("/{userIds}")
    public Result deleteByUserIds(@PathVariable("userIds") List<Long> userIds) {
        userService.removeByUserIds(userIds);
        return Result.success();
    }


}
