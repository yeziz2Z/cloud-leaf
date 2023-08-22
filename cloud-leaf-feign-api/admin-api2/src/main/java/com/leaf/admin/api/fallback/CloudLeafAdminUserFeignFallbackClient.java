package com.leaf.admin.api.fallback;

import com.leaf.admin.api.CloudLeafAdminUserFeignClient;
import com.leaf.admin.pojo.bo.CloudLeafAdminUserBO;
import com.leaf.admin.pojo.dto.CloudLeafAdminUsernamePasswordCaptchaDTO;
import com.leaf.common.result.Result;
import com.leaf.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author liuk
 */
@Component
@Slf4j
public class CloudLeafAdminUserFeignFallbackClient implements CloudLeafAdminUserFeignClient {
    @Override
    public Result<CloudLeafAdminUserBO> getUserAuthorities(CloudLeafAdminUsernamePasswordCaptchaDTO usernamePasswordCaptchaDTO) {
        log.error("调用系统用户服务异常");
        return Result.result(ResultCode.FEIGN_DEGRADATION);
    }

    @Override
    public Result<Void> logout(Map<String, String> headers) {
        log.error("调用系统用户服务异常");
        return Result.result(ResultCode.FEIGN_DEGRADATION);
    }
}
