package com.leaf.admin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.leaf.admin.common.SystemConst;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

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
        JWT jwt = JWT
                .create()
                .setHeader("type", "JWT")
                .setSubject(userName)
                .setIssuedAt(DateUtil.date(now))
                .setExpiresAt(DateUtil.date(expireDate));
        return jwt.sign(JWTSignerUtil.hs512(secret.getBytes()));
    }

    public String generateToken(LocalDateTime issued, LocalDateTime expires, Map<String, Object> payloads) {
        JWT jwt = JWT
                .create()
                .setHeader("type", "JWT")
                .addPayloads(payloads)
                .setIssuedAt(DateUtil.date(issued))
                .setExpiresAt(DateUtil.date(expires));
        return jwt.sign(JWTSignerUtil.hs512(secret.getBytes()));
    }


    public String generateAccessToken(Map payload) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate = now.plus(expire, ChronoUnit.SECONDS);
        payload.put("tokenType", SystemConst.ACCESS_TOKEN);
        return generateToken(now, expireDate, payload);
    }

    public String generateRefreshToken(Map payload) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDate = now.plus(30 * 2, ChronoUnit.SECONDS);
        payload.put("tokenType", SystemConst.REFRESH_TOKEN);
        return generateToken(now, expireDate, payload);
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
