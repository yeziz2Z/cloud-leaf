package com.leaf.admin.async.factory;

import cn.hutool.extra.spring.SpringUtil;
import com.leaf.admin.entity.SysLoginLog;
import com.leaf.admin.entity.SysOperLog;
import com.leaf.admin.service.ISysLoginLogService;
import com.leaf.admin.service.ISysOperLogService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogTaskFactory {

    public static Runnable loginLog(final SysLoginLog loginLog) {
        return () -> SpringUtil.getBean(ISysLoginLogService.class).save(loginLog);
    }

    public static Runnable operationLog(final SysOperLog operLog) {
        return () -> SpringUtil.getBean(ISysOperLogService.class).save(operLog);
    }

}
