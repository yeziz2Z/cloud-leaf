package com.leaf.admin.async.factory;


import com.leaf.admin.sys.entity.SysLoginLog;
import com.leaf.admin.sys.entity.SysOperLog;
import com.leaf.admin.sys.service.ISysLoginLogService;
import com.leaf.admin.sys.service.ISysOperLogService;
import com.leaf.admin.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogTaskFactory {

    public static Runnable loginLog(final SysLoginLog loginLog) {
        return () -> SpringContextUtil.getBean(ISysLoginLogService.class).save(loginLog);
    }

    public static Runnable operationLog(final SysOperLog operLog) {
        return () -> SpringContextUtil.getBean(ISysOperLogService.class).save(operLog);
    }

}
