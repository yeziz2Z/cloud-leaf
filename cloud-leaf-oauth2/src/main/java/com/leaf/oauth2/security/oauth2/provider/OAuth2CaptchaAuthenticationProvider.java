/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leaf.oauth2.security.oauth2.provider;

import com.leaf.admin.api.CloudLeafAdminUserFeignClient;
import com.leaf.admin.pojo.bo.CloudLeafAdminUserBO;
import com.leaf.admin.pojo.dto.CloudLeafAdminUsernamePasswordCaptchaDTO;
import com.leaf.common.result.Result;
import com.leaf.common.result.ResultCode;
import com.leaf.oauth2.security.oauth2.authentication.CloudLeafAdminUserAuthenticationToken;
import com.leaf.oauth2.security.oauth2.authentication.OAuth2CloudLeafAdminCaptchaAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 验证码认证
 *
 * @author liuk
 */
public final class OAuth2CaptchaAuthenticationProvider implements AuthenticationProvider {

    private final CloudLeafAdminUserFeignClient cloudLeafAdminUserFeignClient;
    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    /**
     * Constructs a {@code ClientSecretAuthenticationProvider} using the provided parameters.
     *
     * @param authorizationService the authorization service
     */
    public OAuth2CaptchaAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            CloudLeafAdminUserFeignClient cloudLeafAdminUserFeignClient,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.authorizationService = authorizationService;
        this.cloudLeafAdminUserFeignClient = cloudLeafAdminUserFeignClient;
        this.tokenGenerator = tokenGenerator;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2CloudLeafAdminCaptchaAuthenticationToken clientAuthentication =
                (OAuth2CloudLeafAdminCaptchaAuthenticationToken) authentication;
        OAuth2ClientAuthenticationToken principal = (OAuth2ClientAuthenticationToken) clientAuthentication.getPrincipal();
        RegisteredClient registeredClient = principal.getRegisteredClient();


        Map<String, String> parameters = clientAuthentication.getAdditionalParameters();
        String username = parameters.get("username");
        String password = parameters.get("password");
        String captcha = parameters.get("captcha");
        String uid = parameters.get("uid");

        CloudLeafAdminUsernamePasswordCaptchaDTO adminDTO =
                new CloudLeafAdminUsernamePasswordCaptchaDTO(username, password, uid, captcha);

        Result<CloudLeafAdminUserBO> userBOResult = this.cloudLeafAdminUserFeignClient.getUserAuthorities(adminDTO);
        if (userBOResult.getCode() != ResultCode.SUCCESS.getCode()) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT,
                    userBOResult.getMsg(), "");
            throw new OAuth2AuthenticationException(error);
        }
        CloudLeafAdminUserBO adminUserBO = userBOResult.getData();

        List<SimpleGrantedAuthority> authorities = adminUserBO.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList();
        Set<String> authorizedScopes = Collections.emptySet();
        if (!CollectionUtils.isEmpty(registeredClient.getScopes())) {
            for (String requestedScope : registeredClient.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(registeredClient.getScopes());
        }

        Authentication authenticationToken = new CloudLeafAdminUserAuthenticationToken(username, adminUserBO.getUserId(), null, authorities);

        AuthorizationGrantType captchaGrantType = new AuthorizationGrantType("captcha");

        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(authenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizedScopes(authorizedScopes)
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizationGrantType(captchaGrantType)
                .authorizationGrant(clientAuthentication);

        DefaultOAuth2TokenContext tokenContext = tokenContextBuilder.build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The principal generator failed to generate the access principal.", "");
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());


        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(username)
                .authorizationGrantType(captchaGrantType)
                .authorizedScopes(authorizedScopes);
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                !principal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {
            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", "");
                throw new OAuth2AuthenticationException(error);
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, principal, accessToken, refreshToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuth2CloudLeafAdminCaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error(
                OAuth2ErrorCodes.INVALID_CLIENT,
                "Client authentication failed: " + parameterName, ""
        );
        throw new OAuth2AuthenticationException(error);
    }

}
