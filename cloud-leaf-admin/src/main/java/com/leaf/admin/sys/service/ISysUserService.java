package com.leaf.admin.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leaf.admin.sys.dto.SysUserDTO;
import com.leaf.admin.sys.dto.UserQueryParam;
import com.leaf.admin.sys.entity.SysMenu;
import com.leaf.admin.sys.entity.SysRole;
import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.vo.UserVO;

import java.util.List;

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

    List<SysRole> getRolesByUserId(Long userId);

    List<SysMenu> getMenusByUserId(Long userId);

    String getUserAuthorities(Long userId);

    List<String> getUserAuthoritiesByUsername(String username);

    void clearUserAuthorities(String username);

    void clearUserAuthoritiesByRoleId(Long roleId);

    void clearUserAuthoritiesByMenuId(Long menuId);

    Page<UserVO> selectSysUserVOPage(Page page, UserQueryParam queryParam);

    void deleteByUserIds(List<Long> userIds);

//    void setUserRoles(UserRoleDTO userRoleDTO);

    void saveUser(SysUserDTO sysUserDTO);

    int resetPassword(SysUserDTO sysUserDTO);

    void updateUser(SysUserDTO sysUserDTO);

    void removeByUserIds(List<Long> userIds);
}
