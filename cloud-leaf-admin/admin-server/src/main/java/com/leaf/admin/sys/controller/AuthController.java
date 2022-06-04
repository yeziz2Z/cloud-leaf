package com.leaf.admin.sys.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.leaf.admin.annotation.OperationLog;
import com.leaf.admin.common.SystemConst;
import com.leaf.admin.common.enums.TokenErrorEnum;
import com.leaf.admin.utils.JwtUtil;
import com.leaf.common.enums.BusinessTypeEnum;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    JwtUtil jwtUtils;

    @OperationLog(module = "系统登录-验证码", businessType = BusinessTypeEnum.SELECT)
    @GetMapping("/captcha")
    public Result captcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 40, 5, 70);

        String code = captcha.getCode();
        String captchaImageBase64 = "data:image/png;base64," + captcha.getImageBase64();
        String uid = RandomUtil.randomString(32);

        redisTemplate.opsForValue().set(SystemConst.CAPTCHA_KEY + uid, code, 60, TimeUnit.SECONDS);

        return Result.success(MapUtil.builder()
                .put("uid", uid)
                .put("captcha", captchaImageBase64)
                .build());
    }

    /**
     * 刷新 访问 token
     *
     * @param refreshToken
     * @return
     */
    @GetMapping("/refreshToken")
    public Result refreshToken(@RequestParam(value = "refreshToken", required = false) String refreshToken) {
        if (StrUtil.isEmpty(refreshToken)) {
            return Result.fail("刷新token失败!");
        }
        JWT jwt = jwtUtils.getJwtFromToken(refreshToken);

        if (!jwtUtils.isValidToken(jwt)) {
            return Result.fail("刷新token失败!");
        }
        if (jwtUtils.isExpired(jwt)) {
            return Result.fail(TokenErrorEnum.EXPIRED_TOKEN.getCode(), TokenErrorEnum.EXPIRED_TOKEN.getMessage());
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
