package com.leaf.admin.sys.service.impl;

import com.leaf.admin.sys.entity.SysJobLog;
import com.leaf.admin.sys.mapper.SysJobLogMapper;
import com.leaf.admin.sys.service.ISysJobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements ISysJobLogService {

}
