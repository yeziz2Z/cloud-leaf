package com.leaf.admin.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.annotation.OperationLog;
import com.leaf.admin.api.FileServiceApi;
import com.leaf.admin.sys.dto.SysUserDTO;
import com.leaf.admin.sys.dto.UserQueryParam;
import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.service.ISysMenuService;
import com.leaf.admin.sys.service.ISysOrganizationService;
import com.leaf.admin.sys.service.ISysRoleService;
import com.leaf.admin.sys.service.ISysUserService;
import com.leaf.admin.sys.vo.UserVO;
import com.leaf.common.enums.BusinessTypeEnum;
import com.leaf.common.pojo.auth.AuthUser;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    FileServiceApi fileServiceApi;

    @GetMapping("/info")
    public Result<UserVO> info(Principal principal, HttpServletRequest request) {
        SysUser sysUser = userService.getByUsername("admin");
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

    @OperationLog(module = "用户管理", businessType = BusinessTypeEnum.SELECT)
    @GetMapping("/service")
//    @PreAuthorize("hasAnyAuthority('system.user.list')")
    public Result list(Page page, UserQueryParam queryParam) {
        log.info("queryParam {}, Page {}", queryParam, page);
        return Result.success(userService.selectSysUserVOPage(page, queryParam));
    }

    @PostMapping
//    @PreAuthorize("hasAnyAuthority('system.user.add')")
    @OperationLog(module = "用户管理", businessType = BusinessTypeEnum.INSERT)
    public Result add(@RequestBody SysUserDTO sysUser) {
        userService.saveUser(sysUser);
        return Result.success();
    }

    @PutMapping
//    @PreAuthorize("hasAnyAuthority('system.user.edit')")
    @OperationLog(module = "用户管理", businessType = BusinessTypeEnum.UPDATE)
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
//    @PreAuthorize("hasAnyAuthority('system.user.delete')")
    @OperationLog(module = "用户管理", businessType = BusinessTypeEnum.DELETE)
    public Result deleteByUserIds(@PathVariable("userIds") List<Long> userIds) {
        userService.removeByUserIds(userIds);
        return Result.success();
    }


    @PutMapping("/profile")
    public Result profile(@RequestBody SysUser sysUser) {
        userService.updateById(sysUser);
        return Result.success();
    }

    @PostMapping("/avatar")
    public Result avatar(@RequestParam("file") MultipartFile file) {
        try {
            Result<String> res = fileServiceApi.upload(file, "avatar");
            String url = res.getData();
            SysUser user = userService.getCurrentUser();
            user.setAvatar(url);
            userService.updateById(user);
            userService.clearUserByName(user.getUsername());
            userService.clearUserById(user.getId());
            return res;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.fail("上传图片失败");
        }

    }

    @GetMapping("/loadByUsername")
    public Result<AuthUser> loadByUsername(@RequestParam String username) {
        SysUser sysUser = userService.getByUsername(username);
        if (sysUser == null) {
            return Result.fail("用户不存在");
        }
        AuthUser user = new AuthUser();
        user.setUserId(sysUser.getId());
        user.setEnabled(sysUser.getStatus());
        user.setUsername(sysUser.getUsername());
        user.setPassword(sysUser.getPassword());
        user.setOrgId(sysUser.getOrgId());
        user.setRoles(roleService.getRolesByUserId(sysUser.getId()).stream().map(sysRole -> sysRole.getCode()).collect(Collectors.toList()));
        return Result.success(user);
    }
}
