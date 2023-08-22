package com.leaf.oauth2.security.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.leaf.admin.api.CloudLeafAdminUserFeignClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuk
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CloudLogoutAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final CloudLeafAdminUserFeignClient adminUserFeignClient;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        OAuth2TokenRevocationAuthenticationToken revocationAuthenticationToken = (OAuth2TokenRevocationAuthenticationToken) authentication;
        OAuth2Authorization authorization = SpringUtil.getBean(OAuth2AuthorizationService.class).findByToken(revocationAuthenticationToken.getToken(), null);
        if (Objects.nonNull(authorization) && Objects.nonNull(authorization.getAccessToken())) {
            OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
            Map<String, String> authHeader = new HashMap<>();
            authHeader.put(HttpHeaders.AUTHORIZATION, String.format("%s %s", accessToken.getTokenType().getValue(), accessToken.getTokenValue()));
            log.info("CloudLogoutAuthenticationSuccessHandler header :{}", authHeader);
            adminUserFeignClient.logout(authHeader);
        }
    }

}
