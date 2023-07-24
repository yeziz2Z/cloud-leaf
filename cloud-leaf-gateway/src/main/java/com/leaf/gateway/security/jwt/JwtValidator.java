/*
package com.leaf.gateway.security.jwt;

import com.leaf.common.constant.SecurityConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

*/
/**
 * jwt 类型校验
 *//*

public class JwtValidator implements OAuth2TokenValidator<Jwt> {

    private RedisTemplate redisTemplate;

    public JwtValidator(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        // 包含 ATI 说明此 token是 refresh token, 该类型token应仅作为获取刷新token时使用
        if (token.getClaims().containsKey(SecurityConstant.JWT_ATI)) {
            OAuth2Error oAuth2Error = createOAuth2Error("Encoded token is not a access token");
            return OAuth2TokenValidatorResult.failure(oAuth2Error);
        }
        if (redisTemplate.hasKey(SecurityConstant.TOKEN_BLACKLIST_PREFIX + token.getId())) {
            OAuth2Error oAuth2Error = createOAuth2Error("token已被禁止访问");
            return OAuth2TokenValidatorResult.failure(oAuth2Error);
        }
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2Error createOAuth2Error(String reason) {
        return new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN, reason,
                null);
    }
}
*/
