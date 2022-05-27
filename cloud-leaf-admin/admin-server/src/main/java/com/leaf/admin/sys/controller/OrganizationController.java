package com.leaf.admin.sys.controller;

import com.leaf.admin.annotation.OperationLog;
import com.leaf.admin.sys.entity.SysOrganization;
import com.leaf.admin.sys.service.ISysOrganizationService;
import com.leaf.admin.sys.vo.OrgTreeVO;
import com.leaf.common.enums.BusinessTypeEnum;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/org")
@Slf4j
public class OrganizationController {

    @Autowired
    ISysOrganizationService organizationService;

    @GetMapping("/tree")
    public Result<List<OrgTreeVO>> orgTree() {
        List<OrgTreeVO> orgTreeVOS = organizationService.selectOrgTreeList();
        log.info("orgTree :{}", orgTreeVOS);
        return Result.success(orgTreeVOS);
    }

    @GetMapping("/getOrganizationTree")
    @OperationLog(module = "组织管理", businessType = BusinessTypeEnum.SELECT)
//    @PreAuthorize("hasAnyAuthority('system.org.list')")
    public Result getOrganizationTree() {
        return Result.success(organizationService.getOrganizationTree());
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        return Result.success(organizationService.getById(id));
    }

    @OperationLog(module = "组织管理", businessType = BusinessTypeEnum.INSERT)
    @PostMapping
//    @PreAuthorize("hasAnyAuthority('system.org.add')")
    public Result save(@RequestBody SysOrganization organization) {
        organizationService.saveOrganization(organization);
        return Result.success();
    }

    @OperationLog(module = "组织管理", businessType = BusinessTypeEnum.UPDATE)
    @PutMapping
//    @PreAuthorize("hasAnyAuthority('system.org.edit')")
    public Result edit(@RequestBody SysOrganization organization) {
        organizationService.updateOrganization(organization);
        return Result.success();
    }

    @OperationLog(module = "组织管理", businessType = BusinessTypeEnum.DELETE)
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('system.org.delete')")
    public Result delete(@PathVariable("id") Long id) {
        organizationService.removeById(id);
        return Result.success();
    }

}
