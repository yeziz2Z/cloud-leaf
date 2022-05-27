package com.leaf.gateway.handler;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.leaf.common.constant.SystemConstant;
import com.leaf.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class CaptchaHandler implements HandlerFunction<ServerResponse> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(100, 40, 5, 70);
        String code = captcha.getCode();
        String captchaImageBase64 = "data:image/png;base64," + captcha.getImageBase64();
        String uid = RandomUtil.randomString(12);

        redisTemplate.opsForValue().set(SystemConstant.VALID_CODE + uid, code, 60, TimeUnit.SECONDS);

        Map<Object, Object> res = MapUtil.builder().put("uid", uid).put("captcha", captchaImageBase64).build();
        return ServerResponse.status(HttpStatus.OK).body(BodyInserters.fromValue(Result.success(res)));
    }
}
