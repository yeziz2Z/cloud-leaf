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
package com.leaf.oauth2.security.oauth2.server.authorization.authentication;

import com.leaf.oauth2.security.userdetails.OAuth2ClientUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * 验证码认证
 *
 * @author liuk
 */
public final class OAuth2CaptchaAuthenticationProvider implements AuthenticationProvider {
    private PasswordEncoder passwordEncoder;

    private final List<OAuth2ClientUserDetailsService> userDetailsServices;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    /**
     * Constructs a {@code ClientSecretAuthenticationProvider} using the provided parameters.
     *
     * @param authorizationService the authorization service
     */
    public OAuth2CaptchaAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            List<OAuth2ClientUserDetailsService> userDetailsServices,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.authorizationService = authorizationService;
        this.userDetailsServices = userDetailsServices;
        this.tokenGenerator = tokenGenerator;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Sets the {@link PasswordEncoder} used to validate
     * the {@link RegisteredClient#getClientSecret() client secret}.
     * If not set, the client secret will be compared using
     * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}.
     *
     * @param passwordEncoder the {@link PasswordEncoder} used to validate the client secret
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuth2CaptchaAuthenticationToken clientAuthentication =
                (OAuth2CaptchaAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken principal = (OAuth2ClientAuthenticationToken) clientAuthentication.getPrincipal();
        RegisteredClient registeredClient = principal.getRegisteredClient();

        String clientId = registeredClient.getClientId();
        OAuth2ClientUserDetailsService userDetailsService = null;
        for (OAuth2ClientUserDetailsService service : this.userDetailsServices) {
            if (service.support(clientId)) {
                userDetailsService = service;
                break;
            }
        }
        if (Objects.isNull(userDetailsService)) {
            throwInvalidClient(clientId);
        }
        Map<String, String> parameters = clientAuthentication.getAdditionalParameters();
        String inputPassword = parameters.get("password");
        String username = parameters.get("username");

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!this.passwordEncoder.matches(inputPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("密码有误！");
        }

        Set<String> authorizedScopes = Collections.emptySet();
        if (!CollectionUtils.isEmpty(registeredClient.getScopes())) {
            for (String requestedScope : registeredClient.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(registeredClient.getScopes());
        }

        AuthorizationGrantType captchaGrantType = new AuthorizationGrantType("captcha");

        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(principal)
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
        return OAuth2CaptchaAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private static void throwInvalidClient(String parameterName) {
        OAuth2Error error = new OAuth2Error(
                OAuth2ErrorCodes.INVALID_CLIENT,
                "Client authentication failed: " + parameterName, ""
        );
        throw new OAuth2AuthenticationException(error);
    }


    private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }
}
