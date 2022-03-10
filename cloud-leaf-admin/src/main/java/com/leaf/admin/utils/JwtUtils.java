package com.leaf.admin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@Data
@ConfigurationProperties(prefix = "leaf.jwt")
public class JwtUtils {

    private long expire;

    private String secret;

    private String header;

    public String generateToken(String userName) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime expireDate = now.plus(expire, ChronoUnit.SECONDS);
        JWT jwt = JWT.create().setHeader("type", "JWT")
                .setSubject(userName)
                .setIssuedAt(DateUtil.date(now))
                .setExpiresAt(DateUtil.date(expireDate));
        return jwt.sign(JWTSignerUtil.hs512(secret.getBytes()));
    }

    public JWT getJwtFromToken(String token) {
        return JWT.of(token);
    }

    public boolean isValidToken(JWT jwt) {
        return jwt.verify(JWTSignerUtil.hs512(secret.getBytes()));
    }

    public boolean isExpired(JWT jwt) {
        try {
            JWTValidator.of(jwt).validateDate();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

}
