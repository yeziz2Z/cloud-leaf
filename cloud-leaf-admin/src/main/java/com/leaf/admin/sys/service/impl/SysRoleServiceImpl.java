package com.leaf.admin.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.sys.entity.SysRole;
import com.leaf.admin.sys.mapper.SysRoleMapper;
import com.leaf.admin.sys.service.ISysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        List<Long> roleIds = baseMapper.selectRoleIdsByUserId(userId);
        return this.listByIds(roleIds);
    }
}
