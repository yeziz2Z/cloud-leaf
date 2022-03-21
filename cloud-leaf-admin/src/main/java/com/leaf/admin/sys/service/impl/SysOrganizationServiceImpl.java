package com.leaf.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leaf.admin.sys.entity.SysOrganization;
import com.leaf.admin.sys.mapper.SysOrganizationMapper;
import com.leaf.admin.sys.service.ISysOrganizationService;
import com.leaf.admin.sys.vo.OrgTreeVO;
import com.leaf.common.enums.YesOrNoEnum;
import org.springframework.stereotype.Service;

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
}
