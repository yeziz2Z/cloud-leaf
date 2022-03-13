package com.leaf.admin.sys.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.leaf.admin.common.SystemConst;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("captcha")
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

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("test")
    public Result test() {
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
        return Result.success();
    }
}
