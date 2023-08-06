package com.leaf.admin.controller;

import com.leaf.admin.annotation.OperationLog;
import com.leaf.admin.dto.MenuQueryParam;
import com.leaf.admin.entity.SysMenu;
import com.leaf.admin.service.ISysMenuService;
import com.leaf.admin.vo.SysMenuVO;
import com.leaf.common.enums.BusinessTypeEnum;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    ISysMenuService menuService;

    @OperationLog(module = "菜单管理", businessType = BusinessTypeEnum.SELECT)
    @GetMapping("/tree")
    public Result<List<SysMenuVO>> tree(MenuQueryParam queryParam) {
        return Result.success(menuService.selectMenuTree(queryParam));
    }

    @GetMapping("/{id}")
    public Result<SysMenu> getMenuById(@PathVariable("id") Long id) {
        return Result.success(menuService.getById(id));
    }

    @OperationLog(module = "菜单管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    public Result<Void> add(@RequestBody SysMenu sysMenu) {
        menuService.saveMenu(sysMenu);
        return Result.success();
    }

    @OperationLog(module = "菜单管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    public Result<Void> edit(@RequestBody SysMenu sysMenu) {
        menuService.updateMenu(sysMenu);
        return Result.success();
    }

    /*@DeleteMapping("/{menuIds}")
    public Result delete(@PathVariable("menuIds") List<Long> menuIds) {
        menuService.deleteByMenuIds(menuIds);
        return Result.success();
    }*/


    @OperationLog(module = "菜单管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable("id") Long id) {
        menuService.deleteById(id);
        return Result.success();
    }
}
