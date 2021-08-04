package com.leaf.admin.sys.controller;

import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.service.ISysUserService;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    ISysUserService sysUserService;

    @GetMapping("test")
    public Result test() {
        List<SysUser> list = sysUserService.list();
        Result result = Result.success();
        result.setData(list);
        return result;
    }
}
