package com.leaf.admin.sys.controller;

import com.leaf.admin.sys.dto.MenuQueryParam;
import com.leaf.admin.sys.entity.SysMenu;
import com.leaf.admin.sys.service.ISysMenuService;
import com.leaf.admin.sys.vo.SysMenuVO;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    ISysMenuService menuService;

    @GetMapping("/tree")
    public Result<List<SysMenuVO>> tree(MenuQueryParam queryParam) {
        return Result.success(menuService.selectMenuTree(queryParam));
    }

    @GetMapping("/{id}")
    public Result<SysMenu> getMenuById(@PathVariable("id") Long id) {
        return Result.success(menuService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('system.menu.add')")
    public Result add(@RequestBody SysMenu sysMenu) {
        menuService.saveMenu(sysMenu);
        return Result.success();
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('system.menu.edit')")
    public Result edit(@RequestBody SysMenu sysMenu) {
        menuService.updateMenu(sysMenu);
        return Result.success();
    }

    /*@DeleteMapping("/{menuIds}")
    public Result delete(@PathVariable("menuIds") List<Long> menuIds) {
        menuService.deleteByMenuIds(menuIds);
        return Result.success();
    }*/

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('system.menu.delete')")
    public Result delete(@PathVariable("id") Long id) {
        menuService.deleteById(id);
        return Result.success();
    }
}
