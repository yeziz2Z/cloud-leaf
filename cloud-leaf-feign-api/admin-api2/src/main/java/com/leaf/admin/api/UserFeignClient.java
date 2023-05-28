package com.leaf.admin.api;

import com.leaf.common.pojo.auth.AuthUser;
import com.leaf.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(value = "cloud-leaf-admin", contextId = "user", path = "/user")
public interface UserFeignClient {

    @GetMapping("/loadByUsername")
    Result<AuthUser> loadByUsername(@RequestParam String username);
}
