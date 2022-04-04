package com.leaf.admin.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.sys.dto.UserQueryParam;
import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.vo.UserVO;
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

    //TODO 数据存在一对多  <collection> 映射  查询结果不正常
    Page<UserVO> selectUserList(IPage<UserVO> page, @Param("query") UserQueryParam query);

    int deleteUserRoleByUserIds(@Param("userIds") List<Long> userIds);

    int insertUserRole(@Param("userId")Long userId ,@Param("roleIds")List<Long> roleIds);
}
