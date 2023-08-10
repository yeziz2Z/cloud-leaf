package com.leaf.admin.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.jwt.JWT;
import com.leaf.admin.annotation.OperationLog;
import com.leaf.admin.common.SystemConst;
import com.leaf.admin.common.enums.TokenErrorEnum;
import com.leaf.admin.pojo.bo.CloudLeafAdminUserBO;
import com.leaf.admin.pojo.dto.CloudLeafAdminUsernamePasswordCaptchaDTO;
import com.leaf.admin.service.ISysUserService;
import com.leaf.admin.utils.JwtUtil;
import com.leaf.common.enums.BusinessTypeEnum;
import com.leaf.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RedisTemplate<String, String> redisTemplate;

    private final ISysUserService sysUserService;
    private final JwtUtil jwtUtils;

    @OperationLog(module = "系统登录-验证码", businessType = BusinessTypeEnum.SELECT)
    @GetMapping("/captcha")
    public Result<Map<Object, Object>> captcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 40, 5, 70);

        String code = captcha.getCode();
        String captchaImageBase64 = "data:image/png;base64," + captcha.getImageBase64();
        String uid = RandomUtil.randomString(32);
        redisTemplate
                .opsForValue()
                .set(String.format(SystemConst.CAPTCHA_KEY, SpringUtil.getApplicationName(), uid),
                        code,
                        60,
                        TimeUnit.SECONDS);

        return Result.success(MapUtil.builder()
                .put("uid", uid)
                .put("captcha", captchaImageBase64)
                .build());
    }


    @GetMapping("getUserAuthorities")
    public Result<CloudLeafAdminUserBO> getUserAuthorities(@Validated CloudLeafAdminUsernamePasswordCaptchaDTO usernamePasswordCaptchaDTO) {
        return Result.success(sysUserService.getUserAuthorities(usernamePasswordCaptchaDTO));
    }


    /**
     * 刷新 访问 token
     */
    @GetMapping("/refreshToken")
    public Result<Map<Object, Object>> refreshToken(@RequestParam(value = "refreshToken", required = false) String refreshToken) {
        if (StrUtil.isEmpty(refreshToken)) {
            return Result.fail("刷新token失败!");
        }
        JWT jwt = jwtUtils.getJwtFromToken(refreshToken);

        if (!jwtUtils.isValidToken(jwt)) {
            return Result.fail("刷新token失败!");
        }
        if (jwtUtils.isExpired(jwt)) {
            return Result.fail(TokenErrorEnum.EXPIRED_TOKEN.getMessage());
        }
        if (!Objects.equals(SystemConst.REFRESH_TOKEN, jwt.getPayload("tokenType"))) {
            return Result.fail("刷新token失败!");
        }
        String name = (String) jwt.getPayload("name");
        Map<String, String> payload = MapUtil.builder("name", name).build();
        return Result.success(MapUtil.builder()
                .put(SystemConst.ACCESS_TOKEN, jwtUtils.generateAccessToken(payload))
//                .put(SystemConst.REFRESH_TOKEN, jwtUtils.generateRefreshToken(payload)) //刷新token 同时也生成新的 refreshToken
                .build());
    }

}
