//package com.leaf.oauth2.controller;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONObject;
//import com.leaf.common.constant.SecurityConstant;
//import com.leaf.common.result.Result;
//import com.leaf.common.web.utils.JwtUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//@RequestMapping("/oauth")
//@Slf4j
//public class AuthController {
//
//    @Autowired
//    TokenEndpoint endpoint;
//    @Autowired
//    StringRedisTemplate redisTemplate;
//
//    @PostMapping("/token")
//    public Result token(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
//        log.info("principal {}, parameters {} ", principal, parameters);
//        return Result.success(endpoint.postAccessToken(principal, parameters).getBody());
//    }
//
//    @PostMapping("/logout")
//    public Result logout() {
//        JSONObject payload = JwtUtil.getJwtPayload();
//        String jti = payload.getStr(SecurityConstant.JWT_JTI); // JWT唯一标识
//        Long expireTime = payload.getLong(SecurityConstant.JWT_EXP); // JWT过期时间戳(单位：秒)
//        if (expireTime != null) {
//            long currentTime = System.currentTimeMillis() / 1000;// 当前时间（单位：秒）
//            if (expireTime > currentTime) { // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
//                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_BLACKLIST_PREFIX + jti, StrUtil.EMPTY, (expireTime - currentTime), TimeUnit.SECONDS);
//            }
//        }
//        return Result.success("注销成功");
//    }
//}
