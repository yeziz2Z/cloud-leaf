package com.leaf.admin.sys.service;

import com.leaf.admin.sys.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leaf.admin.sys.vo.SysMenuVO;

import java.util.List;

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

    List<SysMenuVO> selectMenuTree();
}
