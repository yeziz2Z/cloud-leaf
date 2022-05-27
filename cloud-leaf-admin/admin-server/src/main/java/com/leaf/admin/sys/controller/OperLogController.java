package com.leaf.admin.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.sys.dto.OperLogQueryParam;
import com.leaf.admin.sys.entity.SysLoginLog;
import com.leaf.admin.sys.entity.SysOperLog;
import com.leaf.admin.sys.service.ISysOperLogService;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@RequestMapping("/operLog")
@RestController
public class OperLogController {

    @Autowired
    ISysOperLogService sysOperLogService;

    @GetMapping("/page")
    public Result page(Page page, OperLogQueryParam queryParam) {
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(StrUtil.isNotEmpty(queryParam.getModule()), SysOperLog::getModule, queryParam.getModule())
                .like(StrUtil.isNotEmpty(queryParam.getOperName()), SysOperLog::getOperName, queryParam.getOperName())
                .eq(StrUtil.isNotEmpty(queryParam.getStatus()), SysOperLog::getStatus, queryParam.getStatus())
                .eq(!Objects.isNull(queryParam.getBusinessType()), SysOperLog::getBusinessType, queryParam.getBusinessType())
                .ge(!Objects.isNull(queryParam.getStartDate()), SysOperLog::getOperTime, queryParam.getStartDate());

        if (!Objects.isNull(queryParam.getEndDate())) {
            wrapper.lt(SysOperLog::getOperTime, queryParam.getEndDate().plus(1, ChronoUnit.DAYS));
        }
        wrapper.orderByDesc(SysOperLog::getOperTime);
        return Result.success(sysOperLogService.page(page, wrapper));
    }

    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable("ids") List<Long> ids) {
        return Result.success();
    }
}


