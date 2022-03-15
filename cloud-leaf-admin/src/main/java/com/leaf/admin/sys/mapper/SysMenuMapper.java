package com.leaf.admin.sys.mapper;

import com.leaf.admin.sys.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

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
}
