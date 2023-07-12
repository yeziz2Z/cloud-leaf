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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.util.Assert;

/**
 * 验证码认证
 *
 * @author liuk
 */
public final class Oauth2CaptchaAuthenticationProvider implements AuthenticationProvider {
    private final RegisteredClientRepository registeredClientRepository;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a {@code ClientSecretAuthenticationProvider} using the provided parameters.
     *
     * @param registeredClientRepository the repository of registered clients
     * @param authorizationService       the authorization service
     */
    public Oauth2CaptchaAuthenticationProvider(RegisteredClientRepository registeredClientRepository,
                                               OAuth2AuthorizationService authorizationService) {
        Assert.notNull(registeredClientRepository, "registeredClientRepository cannot be null");
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        this.registeredClientRepository = registeredClientRepository;
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

        /*if (!ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(clientAuthentication.getClientAuthenticationMethod()) &&
                !ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(clientAuthentication.getClientAuthenticationMethod())) {
            return null;
        }

        String clientId = clientAuthentication.getPrincipal().toString();
        RegisteredClient registeredClient = this.registeredClientRepository.findByClientId(clientId);
        if (registeredClient == null) {
            throwInvalidClient(OAuth2ParameterNames.CLIENT_ID);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Retrieved registered client");
        }

        if (!registeredClient.getClientAuthenticationMethods().contains(
                clientAuthentication.getClientAuthenticationMethod())) {
            throwInvalidClient("authentication_method");
        }

        if (clientAuthentication.getCredentials() == null) {
            throwInvalidClient("credentials");
        }

        String clientSecret = clientAuthentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(clientSecret, registeredClient.getClientSecret())) {
            throwInvalidClient(OAuth2ParameterNames.CLIENT_SECRET);
        }

        if (registeredClient.getClientSecretExpiresAt() != null &&
                Instant.now().isAfter(registeredClient.getClientSecretExpiresAt())) {
            throwInvalidClient("client_secret_expires_at");
        }

        if (this.passwordEncoder.upgradeEncoding(registeredClient.getClientSecret())) {
            registeredClient = RegisteredClient.from(registeredClient)
                    .clientSecret(this.passwordEncoder.encode(clientSecret))
                    .build();
            this.registeredClientRepository.save(registeredClient);
        }

        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Validated client authentication parameters");
        }



        if (this.logger.isTraceEnabled()) {
            this.logger.trace("Authenticated client secret");
        }

        return new OAuth2CaptchaAuthenticationToken("captcha",
                clientAuthentication.getClientAuthenticationMethod(), clientAuthentication.getCredentials());*/
        return clientAuthentication;
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
