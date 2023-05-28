package com.leaf.admin.sys.service.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.sys.entity.SysOrganization;
import com.leaf.admin.sys.entity.SysUser;
import com.leaf.admin.sys.mapper.SysOrganizationMapper;
import com.leaf.admin.sys.mapper.SysUserMapper;
import com.leaf.admin.sys.service.ISysOrganizationService;
import com.leaf.admin.sys.vo.OrgTreeVO;
import com.leaf.common.enums.YesOrNoEnum;
import com.leaf.common.exception.BusinessException;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author liuk
 * @since 2021-08-04
 */
@Service
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization> implements ISysOrganizationService {

    @Resource
    SysUserMapper userMapper;

    @Override
    public List<OrgTreeVO> selectOrgTreeList() {
        List<SysOrganization> sysOrganizations = this.list(new LambdaQueryWrapper<SysOrganization>()
                .eq(SysOrganization::getDelFlag, YesOrNoEnum.NO.getCode())
                .eq(SysOrganization::getStatus, YesOrNoEnum.YES.getCode())
                .orderByAsc(SysOrganization::getOrderNo));

        List<OrgTreeVO> orgTreeVOList = convert(sysOrganizations);

        return buildTree(orgTreeVOList);
    }

    private List<OrgTreeVO> buildTree(List<OrgTreeVO> orgTreeVOList) {
        List<OrgTreeVO> result = new ArrayList<>();
        orgTreeVOList.forEach(orgTreeVO -> {
            for (OrgTreeVO treeVO : orgTreeVOList) {
                if (Objects.equals(orgTreeVO.getKey(), treeVO.getParentId())) {
                    if (orgTreeVO.getChildren() == null) {
                        orgTreeVO.setChildren(new LinkedList<>());
                    }
                    orgTreeVO.getChildren().add(treeVO);
                }
            }
            if (Objects.equals(orgTreeVO.getParentId(), 0L)) {
                result.add(orgTreeVO);
            }
        });
        return result;
    }

    /**
     * 类型转换
     *
     * @param sysOrganizations
     * @return
     */
    private List<OrgTreeVO> convert(List<SysOrganization> sysOrganizations) {

        return sysOrganizations.stream().map(sysOrganization -> {
            OrgTreeVO orgTreeVO = new OrgTreeVO();
            orgTreeVO.setKey(sysOrganization.getId());
            orgTreeVO.setParentId(sysOrganization.getParentId());
            orgTreeVO.setTitle(sysOrganization.getName());
            return orgTreeVO;
        }).collect(Collectors.toList());

    }

    @Override
    public List<Tree<Long>> getOrganizationTree() {
        List<SysOrganization> sysOrganizations = this.list(new LambdaQueryWrapper<SysOrganization>()
                .eq(SysOrganization::getDelFlag, YesOrNoEnum.NO.getCode()));

        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("id");
        treeNodeConfig.setWeightKey("orderNo");

        List<Tree<Long>> treeList = TreeUtil.build(sysOrganizations, 0L, treeNodeConfig, ((organization, treeNode) -> {
            treeNode.setId(organization.getId());
            treeNode.setParentId(organization.getParentId());
            treeNode.setName(organization.getName());
            treeNode.putExtra("createTime", organization.getCreateTime());
            treeNode.putExtra("status", organization.getStatus());
            treeNode.putExtra("orderNo", organization.getOrderNo());
        }));

        return treeList;
    }

    @Transactional
    @Override
    public void saveOrganization(SysOrganization organization) {
        checkOrganizationName(organization);
        this.save(organization);
    }

    @Transactional
    @Override
    public void updateOrganization(SysOrganization organization) {
        checkOrganizationName(organization);
        super.updateById(organization);
    }

    @Override
    public boolean removeById(Serializable id) {
        if (super.count(new LambdaQueryWrapper<SysOrganization>().eq(SysOrganization::getParentId, id)) > 0) {
            throw new BusinessException(500, "存在下级机构,不允许删除");
        }
        if (userMapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getOrgId, id)) > 0) {
            throw new BusinessException(500, "机构存在用户,不允许删除");
        }
        return super.removeById(id);
    }

    private void checkOrganizationName(SysOrganization organization) {
        LambdaQueryWrapper<SysOrganization> wrapper = new LambdaQueryWrapper<SysOrganization>()
                .ne(!Objects.isNull(organization.getId()), SysOrganization::getId, organization.getId())
                .eq(SysOrganization::getParentId, organization.getParentId())
                .eq(SysOrganization::getName, organization.getName());

        if (super.count(wrapper) > 0) {
            throw new BusinessException(500, "机构名称已存在");
        }

    }
}
