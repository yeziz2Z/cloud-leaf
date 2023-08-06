package com.leaf.admin.controller;

import cn.hutool.core.lang.tree.Tree;
import com.leaf.admin.annotation.OperationLog;
import com.leaf.admin.entity.SysOrganization;
import com.leaf.admin.service.ISysOrganizationService;
import com.leaf.admin.vo.OrgTreeVO;
import com.leaf.common.enums.BusinessTypeEnum;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/org")
@Slf4j
public class OrganizationController {
    ISysOrganizationService organizationService;
    @Autowired
    public OrganizationController(ISysOrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping("/tree")
    public Result<List<OrgTreeVO>> orgTree() {
        List<OrgTreeVO> orgTreeVOS = organizationService.selectOrgTreeList();
        log.info("orgTree :{}", orgTreeVOS);
        return Result.success(orgTreeVOS);
    }

    @GetMapping("/organizationTree")
    @OperationLog(module = "组织管理", businessType = BusinessTypeEnum.SELECT)
    @PreAuthorize("@pms.hasPermission('system.org.list')")
    public Result<List<Tree<Long>>> getOrganizationTree() {
        return Result.success(organizationService.getOrganizationTree());
    }

    @GetMapping("/{id}")
    public Result<SysOrganization> getById(@PathVariable("id") Long id) {
        return Result.success(organizationService.getById(id));
    }

    @OperationLog(module = "组织管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
    @PreAuthorize("@pms.hasPermission('system.org.add')")
    public Result<Void> save(@RequestBody SysOrganization organization) {
        organizationService.saveOrganization(organization);
        return Result.success();
    }

    @OperationLog(module = "组织管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
    @PreAuthorize("@pms.hasPermission('system.org.edit')")
    public Result<Void> edit(@RequestBody SysOrganization organization) {
        organizationService.updateOrganization(organization);
        return Result.success();
    }

    @OperationLog(module = "组织管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('system.org.delete')")
    public Result<Void> delete(@PathVariable("id") Long id) {
        organizationService.removeById(id);
        return Result.success();
    }

}
