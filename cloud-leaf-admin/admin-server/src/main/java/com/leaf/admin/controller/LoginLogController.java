package com.leaf.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.dto.LoginLogQueryParam;
import com.leaf.admin.entity.SysLoginLog;
import com.leaf.admin.service.ISysLoginLogService;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@RequestMapping("/loginLog")
@RestController
public class LoginLogController {

    @Autowired
    ISysLoginLogService loginLogService;

    @GetMapping("/page")
    public Result page(Page page, LoginLogQueryParam queryParam) {
        LambdaQueryWrapper<SysLoginLog> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StrUtil.isNotEmpty(queryParam.getUsername()), SysLoginLog::getUserName, queryParam.getUsername())
                .like(StrUtil.isNotEmpty(queryParam.getIpaddr()), SysLoginLog::getIpaddr, queryParam.getIpaddr())
                .eq(StrUtil.isNotEmpty(queryParam.getStatus()), SysLoginLog::getStatus, queryParam.getStatus())
                .ge(!Objects.isNull(queryParam.getStartDate()), SysLoginLog::getAccessTime, queryParam.getStartDate());

        if (!Objects.isNull(queryParam.getEndDate())) {
            wrapper.lt(SysLoginLog::getAccessTime, queryParam.getEndDate().plus(1, ChronoUnit.DAYS));
        }
        wrapper.orderByDesc(SysLoginLog::getAccessTime);

        return Result.success(loginLogService.page(page, wrapper));
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable("ids") List<Long> ids) {
        return Result.success();
    }
}
