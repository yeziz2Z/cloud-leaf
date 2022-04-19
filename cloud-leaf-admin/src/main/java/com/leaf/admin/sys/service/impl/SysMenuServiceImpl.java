package com.leaf.admin.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.sys.dto.MenuQueryParam;
import com.leaf.admin.sys.entity.SysMenu;
import com.leaf.admin.sys.mapper.SysMenuMapper;
import com.leaf.admin.sys.service.ISysMenuService;
import com.leaf.admin.sys.vo.SysMenuVO;
import com.leaf.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
@Slf4j
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Override
    public List<SysMenu> selectByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public List<SysMenuVO> selectMenuTree(MenuQueryParam queryParam) {

        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<SysMenu>()
                .like(StrUtil.isNotEmpty(queryParam.getTitle()), SysMenu::getTitle, queryParam.getTitle())
                .eq(!Objects.isNull(queryParam.getStatus()), SysMenu::getStatus, queryParam.getStatus())
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getOrderNo);
        List<SysMenu> list = super.list(sysMenuLambdaQueryWrapper);
        return buildMenuTree(list);
    }

    @Override
    public Set<String> selectUserPermissions(Long userId) {
        return baseMapper.selectUserPermissionsByUserId(userId);
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
            if (0L == sysMenuVO.getParentId()) {
                result.add(sysMenuVO);
            }
        });
        return result;
    }

    @Transactional
    @Override
    public void saveMenu(SysMenu sysMenu) {
        if (Objects.equals(sysMenu.getType(), "F")) {
            sysMenu.setComponent(sysMenu.getHiddenHeaderContent() ? "RouteView" : "PageView");
        }
        this.save(sysMenu);
    }

    @Transactional
    @Override
    public void updateMenu(SysMenu sysMenu) {
        if (Objects.equals(sysMenu.getType(), "F")) {
            sysMenu.setComponent(sysMenu.getHiddenHeaderContent() ? "RouteView" : "PageView");
        }
        this.updateById(sysMenu);
    }

    @Transactional
    @Override
    public void deleteByMenuIds(List<Long> menuIds) {
        if (CollectionUtil.isEmpty(menuIds)) {
            return;
        }
        Set<Long> removeIds = new HashSet<>(menuIds);
        for (Long menuId : menuIds) {
            removeIds.addAll(baseMapper.selectSubIds(menuId));
        }
        super.removeByIds(removeIds);
    }


    @Override
    public void deleteById(Long id) {
        long count = super.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (count > 0) {
            throw new BusinessException(500, "存在子菜单，不允许删除");
        }
        if (baseMapper.countRoleMenuByMenuId(id) > 0) {
            throw new BusinessException(500, "菜单已分配，不允许删除");
        }
        super.removeById(id);
    }
}
