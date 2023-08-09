package com.leaf.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.annotation.OperationLog;
import com.leaf.admin.pojo.dto.SysUserForm;
import com.leaf.admin.pojo.dto.UserQueryParam;
import com.leaf.admin.entity.SysMenu;
import com.leaf.admin.entity.SysRole;
import com.leaf.admin.entity.SysUser;
import com.leaf.admin.service.ISysMenuService;
import com.leaf.admin.service.ISysOrganizationService;
import com.leaf.admin.service.ISysRoleService;
import com.leaf.admin.service.ISysUserService;
import com.leaf.admin.pojo.vo.UserVO;
import com.leaf.common.enums.BusinessTypeEnum;
import com.leaf.common.pojo.auth.AuthUser;
import com.leaf.common.result.Result;
import com.leaf.common.validation.group.Create;
import com.leaf.common.validation.group.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/user")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final ISysUserService userService;

    private final ISysRoleService roleService;

    private final ISysMenuService menuService;

    private final ISysOrganizationService organizationService;
    /*@Autowired
    FileServiceApi fileServiceApi;*/

    @GetMapping("/info")
    public Result<UserVO> info() {
        SysUser sysUser = userService.getCurrentUser();
        // 初始化 用户权限
        userService.getUserPermissionUrlsById(sysUser.getId());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser, userVO);
        userVO.setRoles(roleService.getRolesByUserId(sysUser.getId()));
        userVO.setPermissions(menuService.selectUserPermissions(sysUser.getId()));
        return Result.success(userVO);
    }


    @GetMapping("/nav")
    public Result<List<SysMenu>> nav() {
        return Result.success(userService.getCurrentUserNav());
    }

    @OperationLog(module = "用户管理", businessType = BusinessTypeEnum.SELECT)
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('system.user.list')")
    public Result<Page<UserVO>> list(Page<UserVO> page, UserQueryParam queryParam) {
        return Result.success(userService.selectSysUserVOPage(page, queryParam));
    }

    @PostMapping
    @OperationLog(module = "用户管理", businessType = BusinessTypeEnum.INSERT)
    @PreAuthorize("@pms.hasPermission('system.user.add')")
    public Result<Void> add(@Validated(Create.class) @RequestBody SysUserForm sysUser) {
        userService.saveUser(sysUser);
        return Result.success();
    }

    @PutMapping
    @OperationLog(module = "用户管理", businessType = BusinessTypeEnum.UPDATE)
    @PreAuthorize("@pms.hasPermission('system.user.edit')")
    public Result<Void> edit(@Validated(Update.class) @RequestBody SysUserForm sysUser) {
        userService.updateUser(sysUser);
        return Result.success();
    }

    @PutMapping("/resetPassword")
    public Result<Void> resetPassword(@RequestBody SysUserForm sysUser) {
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
    @OperationLog(module = "用户管理", businessType = BusinessTypeEnum.DELETE)
    @PreAuthorize("@pms.hasPermission('system.user.delete')")
    public Result<Void> deleteByUserIds(@PathVariable("userIds") List<Long> userIds) {
        userService.removeByUserIds(userIds);
        return Result.success();
    }


    @PutMapping("/profile")
    public Result<Void> profile(@RequestBody SysUser sysUser) {
        userService.updateById(sysUser);
        return Result.success();
    }

    @PostMapping("/avatar")
    public Result<String> avatar(@RequestParam("file") MultipartFile file) {
        try {
//            Result<String> res = fileServiceApi.upload(file, "avatar");
            Result<String> res = Result.success();
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
        user.setRoles(roleService.getRolesByUserId(sysUser.getId()).stream().map(SysRole::getCode).collect(Collectors.toList()));
        return Result.success(user);
    }
}
