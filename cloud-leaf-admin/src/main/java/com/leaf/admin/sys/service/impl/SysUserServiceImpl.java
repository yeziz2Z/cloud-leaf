package com.leaf.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.sys.entity.SysMenu;
import com.leaf.admin.sys.entity.SysRole;
import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.mapper.SysMenuMapper;
import com.leaf.admin.sys.mapper.SysRoleMapper;
import com.leaf.admin.sys.mapper.SysUserMapper;
import com.leaf.admin.sys.service.ISysUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    RedisTemplate redisTemplate;

    @Resource
    SysRoleMapper roleMapper;
    @Resource
    SysMenuMapper menuMapper;

    private static final String GRANTED_AUTHORITY_KEY = "GrantedAuthority:username:";
    private static final String AUTHORITIES_KEY = "authorities:username:";

    @Cacheable(cacheNames = {"sysUser:username"}, key = "#username")
    @Override
    public SysUser getByUsername(String username) {
        return getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Cacheable(cacheNames = {"sysUser:id"}, key = "#id")
    @Override
    public SysUser getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public String getUserAuthorities(Long userId) {
        SysUser sysUser = this.getById(userId);

        List<String> authorities = this.getUserAuthoritiesByUsername(sysUser.getUsername());
        String grantedAuthority = authorities.stream().collect(Collectors.joining(","));

        return grantedAuthority;
    }

    @Override
    public List<String> getUserAuthoritiesByUsername(String username) {
        String key = AUTHORITIES_KEY + username;
        if (redisTemplate.hasKey(key)) {
            return (List<String>) redisTemplate.opsForList().index(key, -1);
        }
        SysUser sysUser = getByUsername(username);
        Long userId = sysUser.getId();
        List<SysRole> roleList = this.getRolesByUserId(userId);
        List<String> authorities = roleList.stream().map(SysRole::getCode).collect(Collectors.toList());

        List<SysMenu> menuList = this.getMenusByUserId(userId);
//        authorities.addAll(menuList.stream().map(SysMenu::getPermission).collect(Collectors.toList()));
        redisTemplate.opsForList().leftPush(key, authorities);
        return authorities;

    }

    @Override
    public List<SysRole> getRolesByUserId(Long userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        return menuMapper.selectByUserId(userId);
    }

    @Override
    public void clearUserAuthorities(String username) {
        redisTemplate.delete(AUTHORITIES_KEY + username);
    }

    @Override
    public void clearUserAuthoritiesByRoleId(Long roleId) {
        List<SysUser> sysUsers = baseMapper.selectByRoleId(roleId);
        sysUsers.forEach(sysUser -> this.clearUserAuthorities(sysUser.getUsername()));
    }

    @Override
    public void clearUserAuthoritiesByMenuId(Long menuId) {
        List<SysRole> sysRoles = roleMapper.selectByMenuId(menuId);
        sysRoles.forEach(sysRole -> this.clearUserAuthoritiesByRoleId(sysRole.getId()));
    }

   /* @Override
    public Page<SysUserVO> selectSysUserVOPage(UserQueryDTO queryDTO) {
        baseMapper.selectUserList(queryDTO, queryDTO);
        return queryDTO;
    }*/

    @Transactional
    @Override
    public void deleteByUserIds(List<Long> userIds) {
        this.removeByIds(userIds);
        baseMapper.deleteUserRoleByUserIds(userIds);
    }

    /*@Transactional
    @Override
    public void setUserRoles(UserRoleDTO userRoleDTO) {
        Long userId = userRoleDTO.getUserId();
        baseMapper.deleteUserRoleByUserIds(Arrays.asList(userId));
        baseMapper.insertUserRole(userRoleDTO);
        SysUser sysUser = getById(userId);
        clearUserAuthorities(sysUser.getUsername());
    }*/
}
