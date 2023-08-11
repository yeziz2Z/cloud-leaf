package com.leaf.admin.api;

import com.leaf.admin.pojo.bo.CloudLeafAdminUserBO;
import com.leaf.admin.pojo.dto.CloudLeafAdminUsernamePasswordCaptchaDTO;
import com.leaf.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(value = "cloud-leaf-admin", contextId = "user", path = "/auth")
public interface CloudLeafAdminUserFeignClient {

    @PostMapping("/getUserAuthorities")
    Result<CloudLeafAdminUserBO> getUserAuthorities(@RequestBody CloudLeafAdminUsernamePasswordCaptchaDTO usernamePasswordCaptchaDTO);
}
