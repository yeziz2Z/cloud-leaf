package com.leaf.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leaf.admin.pojo.bo.CloudLeafAdminUserBO;
import com.leaf.admin.pojo.dto.CloudLeafAdminUsernamePasswordCaptchaDTO;
import com.leaf.admin.pojo.dto.ResetPasswordDTO;
import com.leaf.admin.pojo.dto.SysUserForm;
import com.leaf.admin.pojo.dto.UserQueryParam;
import com.leaf.admin.entity.SysMenu;
import com.leaf.admin.entity.SysRole;
import com.leaf.admin.entity.SysUser;
import com.leaf.admin.pojo.vo.UserVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
public interface ISysUserService extends IService<SysUser> {

    SysUser getByUsername(String username);

    SysUser getCurrentUser();

    List<SysRole> getRolesByUserId(Long userId);

    List<SysMenu> getMenusByUserId(Long userId);

    List<SysMenu> getCurrentUserNav();

    String getUserAuthorities(Long userId);

    List<String> getUserAuthoritiesByUsername(String username);

    Set<String> getUserPermissionUrlsById(Long userId);

    Set<String> getUserPermissionsByUsername(String username);

    void clearUserAuthorities(String username);

    void clearUserAuthoritiesByRoleId(Long roleId);

    void clearUserMenuByUserId(Long userId);

    void clearUserByName(String name);

    void clearUserById(Long id);

    void clearUserByIds(Collection<Long> ids);

    Page<UserVO> selectSysUserVOPage(Page<UserVO> page, UserQueryParam queryParam);

    void deleteByUserIds(List<Long> userIds);

    void saveUser(SysUserForm sysUserForm);

    boolean resetPassword(ResetPasswordDTO resetPasswordDTO);

    void updateUser(SysUserForm sysUserForm);

    void removeByUserIds(List<Long> userIds);

    CloudLeafAdminUserBO getUserAuthorities(CloudLeafAdminUsernamePasswordCaptchaDTO usernamePasswordCaptchaDTO);
}
