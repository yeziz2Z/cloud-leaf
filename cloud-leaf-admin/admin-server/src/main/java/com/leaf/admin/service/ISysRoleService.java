package com.leaf.admin.service;

import com.leaf.admin.pojo.dto.RoleMenuDTO;
import com.leaf.admin.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
public interface ISysRoleService extends IService<SysRole> {


    List<SysRole> getRolesByUserId(Long userId);

    boolean saveSysRole(SysRole sysRole);

    boolean updateSysRole(SysRole sysRole);

    List<Long> selectMenuIdsByRoleId(Long roleId);

    void saveRoleMenu(RoleMenuDTO roleMenuDTO);

}
