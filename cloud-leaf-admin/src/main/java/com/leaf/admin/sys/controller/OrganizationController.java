package com.leaf.admin.sys.controller;

import com.leaf.admin.sys.service.ISysOrganizationService;
import com.leaf.admin.sys.vo.OrgTreeVO;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
