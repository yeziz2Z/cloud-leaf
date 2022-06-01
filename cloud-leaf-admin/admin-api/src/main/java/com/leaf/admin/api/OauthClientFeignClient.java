package com.leaf.admin.api;

import com.leaf.common.pojo.auth.Oauth2Client;
import com.leaf.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "cloud-leaf-admin", contextId = "oauth-clients", path = "/oauth-clients")
public interface OauthClientFeignClient {


    @GetMapping("/getOauth2ClientById")
    Result<Oauth2Client> getOauth2ClientById(@RequestParam String clientId);
}
