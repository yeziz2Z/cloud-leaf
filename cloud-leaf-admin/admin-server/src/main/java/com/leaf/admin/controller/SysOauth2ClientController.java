package com.leaf.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leaf.admin.entity.SysOauth2Client;
import com.leaf.admin.service.ISysOauth2ClientService;
import com.leaf.common.pojo.auth.Oauth2Client;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/oauth-clients")
public class SysOauth2ClientController {

    @Autowired
    ISysOauth2ClientService oauth2ClientService;

    @GetMapping("/{clientId}")
    public Result<SysOauth2Client> getById(@PathVariable("clientId") String clientId) {
        return Result.success(oauth2ClientService.getById(clientId));
    }

    @GetMapping("/clientId")
    public Result<SysOauth2Client> checkClientId(Long id, String clientId) {
        LambdaQueryWrapper<SysOauth2Client> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(id != null, SysOauth2Client::getId, id)
                .eq(SysOauth2Client::getClientId, clientId);
        long count = oauth2ClientService.count(wrapper);
        if (count > 0) {
            return Result.fail(clientId + "已存在！");
        }
        return Result.success();
    }

    @GetMapping("/page")
    public Result<Page> page(Page page, String clientId) {
        LambdaQueryWrapper<SysOauth2Client> wrapper = new LambdaQueryWrapper<SysOauth2Client>().eq(StrUtil.isNotEmpty(clientId), SysOauth2Client::getClientId, clientId);
        return Result.success(oauth2ClientService.page(page, wrapper));
    }

    @PostMapping
    public Result save(@RequestBody SysOauth2Client client) {
        log.debug("client param: {}", client);
        oauth2ClientService.save(client);
        return Result.success();
    }

    @PutMapping
    public Result edit(@RequestBody SysOauth2Client client) {
        oauth2ClientService.updateById(client);
        return Result.success();
    }

    @DeleteMapping("/{clientIds}")
    public Result<SysOauth2Client> deleteByClientIds(@PathVariable("clientIds") List<String> clientIds) {
        oauth2ClientService.removeBatchByIds(clientIds);
        return Result.success();
    }

    @GetMapping("/getOauth2ClientById")
    public Result<Oauth2Client> getOauth2ClientById(@RequestParam String clientId) {
        SysOauth2Client oauth2Client = oauth2ClientService.getOne(new LambdaQueryWrapper<SysOauth2Client>().eq(SysOauth2Client::getClientId, clientId));
        if (oauth2Client == null) {
            return Result.fail("OAuth2 客户端不存在");
        }
        Oauth2Client client = new Oauth2Client();
        BeanUtil.copyProperties(oauth2Client, client);
        return Result.success(client);
    }


}
