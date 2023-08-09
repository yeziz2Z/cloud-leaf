package com.leaf.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leaf.admin.entity.SysOrganization;
import com.leaf.admin.pojo.vo.OrgTreeVO;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
public interface ISysOrganizationService extends IService<SysOrganization> {

    List<OrgTreeVO> selectOrgTreeList();

    List<Tree<Long>> getOrganizationTree();

    void saveOrganization(SysOrganization organization);

    void updateOrganization(SysOrganization organization);

}
