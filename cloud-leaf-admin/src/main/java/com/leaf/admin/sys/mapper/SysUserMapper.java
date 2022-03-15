package com.leaf.admin.sys.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.sys.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysUser> selectByRoleId(Long roleId);

//    Page<SysUserVO> selectUserList(Page page, @Param("query") UserQueryDTO query);

    int deleteUserRoleByUserIds(List<Long> userIds);

//    int insertUserRole(UserRoleDTO userRoleDTO);
}
