package com.leaf.admin.api;

import com.leaf.admin.pojo.bo.CloudLeafAdminUserBO;
import com.leaf.admin.pojo.dto.CloudLeafAdminUsernamePasswordCaptchaDTO;
import com.leaf.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
@FeignClient(value = "cloud-leaf-admin", contextId = "user", path = "/auth")
public interface CloudLeafAdminUserFeignClient {

    @GetMapping("/getUserAuthorities")
    Result<CloudLeafAdminUserBO> getUserAuthorities(@SpringQueryMap CloudLeafAdminUsernamePasswordCaptchaDTO usernamePasswordCaptchaDTO);
}
