package com.leaf.admin.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.sys.dto.RoleMenuDTO;
import com.leaf.admin.sys.dto.RoleQueryParam;
import com.leaf.admin.sys.entity.SysRole;
import com.leaf.admin.sys.service.ISysRoleService;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    ISysRoleService roleService;

    @GetMapping("/list")
    public Result list() {
        return Result.success(roleService.list());
    }

    @GetMapping("/page")
    public Result page(Page page, RoleQueryParam queryParam) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotEmpty(queryParam.getName()), SysRole::getName, queryParam.getName())
                .eq(StrUtil.isNotEmpty(queryParam.getCode()), SysRole::getCode, queryParam.getCode())
                .orderByAsc(SysRole::getOrderNo);

        roleService.page(page, queryWrapper);
        return Result.success(page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result add(@RequestBody SysRole sysRole) {
        roleService.saveSysRole(sysRole);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SysRole> getByRoleId(@PathVariable("id") Long id) {
        return Result.success(roleService.getById(id));
    }

    @PutMapping
    public Result edit(@RequestBody SysRole sysRole) {
        roleService.updateSysRole(sysRole);
        return Result.success();
    }

    @DeleteMapping("/{roleIds}")
    public Result delete(@PathVariable("roleIds") List<Long> roleIds) {
        roleService.removeBatchByIds(roleIds);
        return Result.success();
    }

    @GetMapping("/menus/{id}")
    public Result<List<Long>> menus(@PathVariable("id") Long roleId) {
        return Result.success(roleService.selectMenuIdsByRoleId(roleId));
    }

    @PostMapping("/menus")
    public Result roleMenus(@RequestBody RoleMenuDTO roleMenuDTO) {
        roleService.saveRoleMenu(roleMenuDTO);
        return Result.success();
    }
}
