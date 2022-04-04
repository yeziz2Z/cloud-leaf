package com.leaf.admin.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.leaf.admin.sys.entity.SysMenu;
import com.leaf.admin.sys.mapper.SysMenuMapper;
import com.leaf.admin.sys.service.ISysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.sys.vo.SysMenuVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> selectByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public List<SysMenuVO> selectMenuTree() {
        List<SysMenu> sysMenus = this.list();
        return buildMenuTree(sysMenus);
    }

    private List<SysMenuVO> buildMenuTree(List<SysMenu> sysMenus) {
        List<SysMenuVO> sysMenuVOS = BeanUtil.copyToList(sysMenus, SysMenuVO.class);
        List<SysMenuVO> result = new LinkedList<>();
        sysMenuVOS.forEach(sysMenuVO -> {
            for (SysMenuVO menuVO : sysMenuVOS) {
                if (Objects.equals(sysMenuVO.getId(), menuVO.getParentId())) {
                    if (Objects.isNull(sysMenuVO.getChildren())) {
                        sysMenuVO.setChildren(new LinkedList<>());
                    }
                    sysMenuVO.getChildren().add(menuVO);
                }
            }
            if (0L == sysMenuVO.getParentId()){
                result.add(sysMenuVO);
            }
        });
        return result;
    }
}
