package com.leaf.admin.mapper;

import com.leaf.admin.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leaf.admin.vo.RolePermissions;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> selectByUserId(Long userId);

    List<SysMenu> listRolePermissions();

    Set<String> selectUserPermissionsByUserId(Long userId);

    List<SysMenu> selectMenusByUserId(Long userId);

    List<Long> selectSubIds(Long menuId);

    int countRoleMenuByMenuId(Long menuId);

    List<RolePermissions> rolePermissions();
}
