package com.leaf.admin.sys.controller;

import com.leaf.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {


    @GetMapping("/info")
    public Result info() {

        return Result.success();
    }


    @GetMapping("/nav")
    public Result nav() {

        return Result.success();
    }
}
