package com.leaf.admin.sys.mapper;

import com.leaf.admin.sys.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectByUserId(Long userId);

    List<SysRole> selectByMenuId(Long menuId);

    int deleteRoleMenuByRoleIds(@Param("roleIds") List<Long> roleIds);

    int deleteUserRoleByRoleIds(@Param("roleIds") List<Long> roleIds);

//    int insertRoleMenu(RolePermissionDTO rolePermissionDTO);
}
