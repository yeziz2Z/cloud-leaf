package com.leaf.admin.service;

import com.leaf.admin.pojo.dto.MenuQueryParam;
import com.leaf.admin.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leaf.admin.pojo.vo.SysMenuVO;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
public interface ISysMenuService extends IService<SysMenu> {
    List<SysMenu> selectByUserId(Long userId);

    List<SysMenuVO> selectMenuTree(MenuQueryParam queryParam);

    Set<String> selectUserPermissions(Long userId);

    void saveMenu(SysMenu sysMenu);

    void updateMenu(SysMenu sysMenu);

    void deleteByMenuIds(List<Long> menuIds);

    void deleteById(Long id);

    void refreshRolePermission();
}
