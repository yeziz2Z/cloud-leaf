package com.leaf.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.common.enums.AdminErrorResultEnum;
import com.leaf.admin.pojo.dto.RoleMenuDTO;
import com.leaf.admin.entity.SysRole;
import com.leaf.admin.mapper.SysRoleMapper;
import com.leaf.admin.service.ISysMenuService;
import com.leaf.admin.service.ISysRoleService;
import com.leaf.admin.service.ISysUserService;
import com.leaf.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final ISysUserService userService;
    private final ISysMenuService menuService;

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        List<Long> roleIds = baseMapper.selectRoleIdsByUserId(userId);
        if (CollectionUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        return this.listByIds(roleIds);
    }

    @Override
    public boolean saveSysRole(SysRole sysRole) {
        long count = this.count(new LambdaQueryWrapper<SysRole>().eq(SysRole::getCode, sysRole.getCode()));
        if (count > 0) {
            throw new BusinessException(AdminErrorResultEnum.ROLE_CODE_EXISTS);
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
            throw new BusinessException(AdminErrorResultEnum.ROLE_CODE_EXISTS);
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

        menuService.refreshRolePermission();
    }

}
