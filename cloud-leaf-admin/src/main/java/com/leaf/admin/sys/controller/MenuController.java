package com.leaf.admin.sys.controller;

import com.leaf.admin.sys.service.ISysMenuService;
import com.leaf.admin.sys.vo.SysMenuVO;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    ISysMenuService menuService;

    @GetMapping("/tree")
    public Result<List<SysMenuVO>> tree() {
        return Result.success(menuService.selectMenuTree());
    }
}
