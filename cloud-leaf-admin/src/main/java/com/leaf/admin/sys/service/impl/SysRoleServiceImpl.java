package com.leaf.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.sys.dto.RoleMenuDTO;
import com.leaf.admin.sys.entity.SysRole;
import com.leaf.admin.sys.mapper.SysRoleMapper;
import com.leaf.admin.sys.service.ISysRoleService;
import com.leaf.admin.sys.service.ISysUserService;
import com.leaf.common.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    ISysUserService userService;

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        List<Long> roleIds = baseMapper.selectRoleIdsByUserId(userId);
        return this.listByIds(roleIds);
    }

    @Override
    public boolean saveSysRole(SysRole sysRole) {
        long count = this.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, sysRole.getCode()));
        if (count > 0) {
            throw new BusinessException(500, "角色编码已存在");
        }
        return this.save(sysRole);
    }

    @Override
    public boolean updateSysRole(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getCode, sysRole.getCode())
                .ne(SysRole::getId, sysRole.getId());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(500, "角色编码已存在");
        }
        return this.updateById(sysRole);
    }

    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        return baseMapper.selectMenuIdsByRoleId(roleId);
    }

    @Transactional
    @Override
    public void saveRoleMenu(RoleMenuDTO roleMenuDTO) {
        baseMapper.deleteRoleMenuByRoleId(roleMenuDTO.getRoleId());

        baseMapper.insertRoleMenu(roleMenuDTO);

        userService.clearUserAuthoritiesByRoleId(roleMenuDTO.getRoleId());
    }
}
