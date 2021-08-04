package com.leaf.admin.sys.service.impl;

import com.leaf.admin.sys.entity.SysJob;
import com.leaf.admin.sys.mapper.SysJobMapper;
import com.leaf.admin.sys.service.ISysJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

}
