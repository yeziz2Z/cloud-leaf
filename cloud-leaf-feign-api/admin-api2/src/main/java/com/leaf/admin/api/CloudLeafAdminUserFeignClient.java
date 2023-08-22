package com.leaf.admin.api;

import com.leaf.admin.api.fallback.CloudLeafAdminUserFeignFallbackClient;
import com.leaf.admin.pojo.bo.CloudLeafAdminUserBO;
import com.leaf.admin.pojo.dto.CloudLeafAdminUsernamePasswordCaptchaDTO;
import com.leaf.common.result.Result;
import com.leaf.config.FeignConfig;
import feign.HeaderMap;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Component
@FeignClient(value = "cloud-leaf-admin", contextId = "user", path = "/auth", fallback = CloudLeafAdminUserFeignFallbackClient.class)
public interface CloudLeafAdminUserFeignClient {


    @PostMapping("/getUserAuthorities")
    Result<CloudLeafAdminUserBO> getUserAuthorities(@RequestBody CloudLeafAdminUsernamePasswordCaptchaDTO usernamePasswordCaptchaDTO);

    @GetMapping("/logout")
    Result<Void> logout(@RequestHeader Map<String, String> headers);
}
