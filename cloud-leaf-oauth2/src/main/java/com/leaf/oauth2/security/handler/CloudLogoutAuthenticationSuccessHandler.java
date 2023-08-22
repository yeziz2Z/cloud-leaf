package com.leaf.oauth2.security.handler;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2TokenRevocationAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author liuk
 */
@Component
@Slf4j
public class CloudLogoutAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("========= logout : {} ", authentication);
        OAuth2TokenRevocationAuthenticationToken revocationAuthenticationToken = (OAuth2TokenRevocationAuthenticationToken) authentication;
        OAuth2Authorization authorization = SpringUtil.getBean(OAuth2AuthorizationService.class).findByToken(revocationAuthenticationToken.getToken(), null);
        OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();


    }
}
