package com.leaf.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.common.SystemConst;
import com.leaf.admin.common.enums.AdminErrorResultEnum;
import com.leaf.admin.entity.SysMenu;
import com.leaf.admin.entity.SysRole;
import com.leaf.admin.entity.SysUser;
import com.leaf.admin.mapper.SysMenuMapper;
import com.leaf.admin.mapper.SysRoleMapper;
import com.leaf.admin.mapper.SysUserMapper;
import com.leaf.admin.pojo.bo.CloudLeafAdminUserBO;
import com.leaf.admin.pojo.dto.CloudLeafAdminUsernamePasswordCaptchaDTO;
import com.leaf.admin.pojo.dto.ResetPasswordDTO;
import com.leaf.admin.pojo.dto.SysUserForm;
import com.leaf.admin.pojo.dto.UserQueryParam;
import com.leaf.admin.pojo.vo.UserVO;
import com.leaf.admin.service.ISysUserService;
import com.leaf.admin.utils.UserUtils;
import com.leaf.common.constant.SecurityConstant;
import com.leaf.common.enums.YesOrNoEnum;
import com.leaf.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
@Slf4j
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;

    private final PasswordEncoder passwordEncoder;

    private static final String AUTHORITIES_KEY = "authorities:username:";
    private static final String USERNAME_KEY = "sys:username:";
    private static final String USER_ID_KEY = "sys:userId:";
    private static final String USER_MENU_KEY = "sys:menus:";

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
        return String.join(",", authorities);
    }

    @Override
    public List<String> getUserAuthoritiesByUsername(String username) {
        String key = AUTHORITIES_KEY + username;

        List<String> authorities = (List<String>) redisTemplate.opsForValue().get(key);
        if (CollUtil.isEmpty(authorities)) {
            SysUser sysUser = getByUsername(username);
            Long userId = sysUser.getId();
            List<SysRole> roleList = this.getRolesByUserId(userId);
            authorities = roleList.stream().map(SysRole::getCode).collect(Collectors.toList());

            List<SysMenu> menuList = this.getMenusByUserId(userId);
            authorities.addAll(menuList.stream()
                    .map(SysMenu::getPermission)
                    .filter(StrUtil::isNotEmpty)
                    .toList());
            redisTemplate.opsForValue().set(key, authorities, 60 * 60, TimeUnit.SECONDS);
        }
        return authorities;

    }

    @Override
    public Set<String> getUserPermissionUrlsById(Long userId) {
        String key = SecurityConstant.USER_PERM_URL_KEY + userId;
        Set<String> permUrls = (Set<String>) redisTemplate.opsForValue().get(key);

        if (CollUtil.isEmpty(permUrls)) {
            List<SysMenu> sysMenus = this.getMenusByUserId(userId);
            permUrls = sysMenus.stream().map(SysMenu::getPermissionUrl).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(permUrls)) {
                redisTemplate.opsForValue().set(key, permUrls, 1, TimeUnit.HOURS);
            }
        }
        return permUrls;
    }

    @Override
    public Set<String> getUserPermissionsByUsername(String username) {
        String key = SecurityConstant.USER_PERMS_KEY + username;
        Set<String> perms = (Set<String>) redisTemplate.opsForValue().get(key);
        if (CollUtil.isEmpty(perms)) {
            SysUser sysUser = this.getByUsername(username);
            perms = this.menuMapper.selectUserPermissionsByUserId(sysUser.getId());
            redisTemplate.opsForValue().set(key, perms, 1, TimeUnit.HOURS);
        }
        return perms;
    }

    private void clearUserPermissionUrls(Long userId) {
        redisTemplate.delete(SecurityConstant.URL_PERM_ROLES_KEY + userId);
    }

    @Override
    public List<SysMenu> getCurrentUserNav() {
        SysUser currentUser = this.getCurrentUser();
        String key = USER_MENU_KEY + currentUser.getId();
        List<SysMenu> result = (List<SysMenu>) redisTemplate.opsForValue().get(key);
        if (CollUtil.isEmpty(result)) {
            // 查询  目录以及菜单
            result = menuMapper.selectMenusByUserId(currentUser.getId());
            redisTemplate.opsForValue().set(key, result, 60 * 60, TimeUnit.SECONDS);
        }
        return result;
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
        sysUsers.forEach(sysUser -> {
            this.clearUserAuthorities(sysUser.getUsername());
            this.clearUserMenuByUserId(sysUser.getId());
            this.clearUserPermissionUrls(sysUser.getId());
        });
    }

    @Override
    public void clearUserByName(String name) {
        redisTemplate.delete(USERNAME_KEY + name);
    }

    @Override
    public void clearUserById(Long id) {
        redisTemplate.delete(USER_ID_KEY + id);
    }

    @Override
    public void clearUserByIds(Collection<Long> ids) {
        ids.forEach(this::clearUserById);
    }

    @Override
    public void clearUserMenuByUserId(Long userId) {
        redisTemplate.delete(USER_MENU_KEY + userId);
    }

    @Override
    public Page<UserVO> selectSysUserVOPage(Page<UserVO> page, UserQueryParam queryParam) {
        return baseMapper.selectUserList(page, queryParam);
    }

    @Transactional
    @Override
    public void deleteByUserIds(List<Long> userIds) {
        this.removeByIds(userIds);
        baseMapper.deleteUserRoleByUserIds(userIds);
    }


    @Override
    @Transactional
    public void saveUser(SysUserForm sysUserForm) {
        long count = this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUserForm.getUsername()));
        if (count > 0) {
            throw new BusinessException(AdminErrorResultEnum.USER_CODE_EXISTS);
        }
        SysUser user = new SysUser();
        BeanUtil.copyProperties(sysUserForm, user, "id");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 入库
        this.save(user);
        // 保存用户角色
        baseMapper.insertUserRole(user.getId(), sysUserForm.getRoleIds());
    }

    @Override
    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO) {
        SysUser sysUser = this.getById(resetPasswordDTO.getUserId());
        if (Objects.isNull(sysUser)) {
            throw new BusinessException(AdminErrorResultEnum.USER_NOT_EXISTS);
        }
        sysUser.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        boolean updated = this.updateById(sysUser);
        if (updated) {
            //TODO 待实现 异步通知 发送邮件&短信
            log.info("重置密码成功.. ");

            this.clearUserById(sysUser.getId());
        }
        return updated;
    }

    @Transactional
    @Override
    public void updateUser(SysUserForm sysUserForm) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, sysUserForm.getUsername())
                .ne(SysUser::getId, sysUserForm.getId());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(AdminErrorResultEnum.USER_CODE_EXISTS);
        }

        SysUser user = new SysUser();
        BeanUtil.copyProperties(sysUserForm, user);
        // 清缓存
        this.clearUserById(user.getId());

        this.updateById(user);
        baseMapper.deleteUserRoleByUserIds(Collections.singletonList(user.getId()));
        // 保存用户角色
        baseMapper.insertUserRole(user.getId(), sysUserForm.getRoleIds());

    }

    @Override
    @Transactional
    public void removeByUserIds(List<Long> userIds) {
        SysUser sysUser = this.getCurrentUser();
        if (CollectionUtil.contains(userIds, sysUser.getId())) {
            throw new BusinessException(AdminErrorResultEnum.CANT_DELETE_CURRENT_USER);
        }

        this.clearUserByIds(userIds);

        baseMapper.deleteUserRoleByUserIds(userIds);

        baseMapper.deleteBatchIds(userIds);
    }

    @Override
    public SysUser getCurrentUser() {
        return getByUsername(UserUtils.getCurrentUsername());
    }

    @Override
    public CloudLeafAdminUserBO getUserAuthorities(CloudLeafAdminUsernamePasswordCaptchaDTO usernamePasswordCaptchaDTO) {
        String uid = usernamePasswordCaptchaDTO.getUid();
        String captcha = (String) redisTemplate
                .opsForValue()
                .getAndDelete(String.format(SystemConst.CAPTCHA_KEY, SpringUtil.getApplicationName(), uid));

        if (StrUtil.isBlank(captcha)) {
            throw new BusinessException(AdminErrorResultEnum.IMG_CAPTCHA_EXPIRED);
        }
        if (!StrUtil.equals(captcha, usernamePasswordCaptchaDTO.getCaptchaCode())) {
            throw new BusinessException(AdminErrorResultEnum.IMG_CAPTCHA_ERROR);
        }
        SysUser sysUser = this.getByUsername(usernamePasswordCaptchaDTO.getUsername());
        if (Objects.isNull(sysUser)
                || StrUtil.equals(YesOrNoEnum.YES.getCode(), sysUser.getDelFlag())
                || !passwordEncoder.matches(usernamePasswordCaptchaDTO.getPassword(), sysUser.getPassword())) {
            throw new BusinessException(AdminErrorResultEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        if (!sysUser.getStatus()) {
            throw new BusinessException(AdminErrorResultEnum.ACCOUNT_OR_PASSWORD_ERROR);
        }
        List<SysRole> sysRoles = roleMapper.selectByUserId(sysUser.getId());
        List<String> roles = sysRoles.stream().map(SysRole::getCode).toList();
        return new CloudLeafAdminUserBO(sysUser.getId(), sysUser.getUsername(), roles);
    }
}
