package com.leaf.admin.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.sys.dto.SysUserDTO;
import com.leaf.admin.sys.dto.UserQueryParam;
import com.leaf.admin.sys.entity.SysMenu;
import com.leaf.admin.sys.entity.SysRole;
import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.mapper.SysMenuMapper;
import com.leaf.admin.sys.mapper.SysRoleMapper;
import com.leaf.admin.sys.mapper.SysUserMapper;
import com.leaf.admin.sys.service.ISysUserService;
import com.leaf.admin.sys.vo.UserVO;
import com.leaf.common.exception.BusinessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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

    @Resource
    PasswordEncoder passwordEncoder;

    private static final String GRANTED_AUTHORITY_KEY = "GrantedAuthority:username:";
    private static final String AUTHORITIES_KEY = "authorities:username:";
    private static final String USERNAME_KEY = "sys:username:";
    private static final String USER_ID_KEY = "sys:userId:";

    @Override
    public SysUser getByUsername(String username) {
        String key = USERNAME_KEY + username;
        SysUser user = (SysUser) redisTemplate.opsForValue().get(key);
        if (Objects.isNull(user)) {
            user = getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
            redisTemplate.opsForValue().set(key, user, 3600, TimeUnit.SECONDS);
        }
        return user;
    }

    @Override
    public SysUser getById(Serializable id) {
        String key = USER_ID_KEY + id;
        SysUser user = (SysUser) redisTemplate.opsForValue().get(key);
        if (Objects.isNull(user)) {
            user = super.getById(id);
            redisTemplate.opsForValue().set(key, user, 3600, TimeUnit.SECONDS);
        }
        return user;
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
    public List<SysMenu> getCurrentUserNav() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SysUser currentUser = this.getByUsername(authentication.getName());
        return menuMapper.selectMenusByUserId(currentUser.getId());
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

    @Override
    public Page<UserVO> selectSysUserVOPage(Page page, UserQueryParam queryParam) {
        return baseMapper.selectUserList(page, queryParam);
    }

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

    @Override
    @Transactional
    @Deprecated
    public void saveUser(SysUserDTO sysUserDTO) {
        SysUser user = new SysUser();
        BeanUtil.copyProperties(sysUserDTO, user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 入库
        this.save(user);
        if (CollectionUtil.isNotEmpty(sysUserDTO.getRoleIds())) {
            // 保存用户角色
            baseMapper.insertUserRole(user.getId(), sysUserDTO.getRoleIds());
        }
    }

    @Override
    public int resetPassword(SysUserDTO sysUserDTO) {
        SysUser user = new SysUser();
        user.setId(sysUserDTO.getId());
        user.setPassword(passwordEncoder.encode(sysUserDTO.getPassword()));
        return baseMapper.updateById(user);
    }

    @Transactional
    @Override
    public void updateUser(SysUserDTO sysUserDTO) {
        SysUser user = new SysUser();
        BeanUtil.copyProperties(sysUserDTO, user);

        this.updateById(user);
        baseMapper.deleteUserRoleByUserIds(Arrays.asList(user.getId()));

        if (CollectionUtil.isNotEmpty(sysUserDTO.getRoleIds())) {
            // 保存用户角色
            baseMapper.insertUserRole(user.getId(), sysUserDTO.getRoleIds());
        }

    }

    @Override
    @Transactional
    public void removeByUserIds(List<Long> userIds) {
        SysUser sysUser = this.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (CollectionUtil.contains(userIds, sysUser.getId())) {
            throw new BusinessException(500, "不能删除当前用户");
        }
        baseMapper.deleteUserRoleByUserIds(userIds);

        baseMapper.deleteBatchIds(userIds);
    }
}
