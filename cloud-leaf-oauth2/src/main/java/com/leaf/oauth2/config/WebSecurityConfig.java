package com.leaf.oauth2.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.json.JSONUtil;
import com.leaf.admin.api.CloudLeafAdminUserFeignClient;
import com.leaf.common.result.Result;
import com.leaf.oauth2.security.authentication.CloudLeafAdminUserAuthenticationToken;
import com.leaf.oauth2.security.convert.CaptchaAuthenticationConverter;
import com.leaf.oauth2.security.provider.OAuth2CaptchaAuthenticationProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.ServletOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@Slf4j
public class WebSecurityConfig {

    @Value("${cloud.jwk.public-key}")
    private String publicKey;
    @Value("${cloud.jwk.private-key}")
    private String privateKey;

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      CloudLeafAdminUserFeignClient cloudLeafAdminUserFeignClient,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      OAuth2TokenGenerator<?> tokenGenerator) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        authorizationServerConfigurer
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
                                .accessTokenRequestConverter(
                                        new CaptchaAuthenticationConverter())
                                .authenticationProvider(
                                        new OAuth2CaptchaAuthenticationProvider(
                                                authorizationService, cloudLeafAdminUserFeignClient, tokenGenerator))
                                // 认证成功处理器
                                .accessTokenResponseHandler((request, response, authentication) -> {
                                    OAuth2AccessTokenAuthenticationToken accessTokenAuthentication =
                                            (OAuth2AccessTokenAuthenticationToken) authentication;

                                    OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
                                    OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();

                                    // TODO 临时处理
                                    Map<Object, Object> data = MapUtil.builder().put("access_token", accessToken.getTokenValue())
                                            .put("token_type", accessToken.getTokenType().getValue())
                                            .put("refresh_token", Objects.requireNonNull(refreshToken).getTokenValue()).build();

                                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    ServletOutputStream outputStream = response.getOutputStream();
                                    outputStream.write(JSONUtil.toJsonStr(Result.success(data)).getBytes(StandardCharsets.UTF_8));
                                    outputStream.flush();
                                    outputStream.close();
                                })
                                // 认证失败处理器
                                .errorResponseHandler((request, response, exception) -> {
                                    log.error("认证失败", exception);
                                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    ServletOutputStream outputStream = response.getOutputStream();
                                    outputStream.write(JSONUtil.toJsonStr(Result.fail(exception.getMessage())).getBytes(StandardCharsets.UTF_8));
                                    outputStream.flush();
                                    outputStream.close();
                                }));
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        http
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .anyRequest().authenticated()
                )
                .exceptionHandling(customizer -> {
                    customizer.accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.error("===============================accessDeniedException ", accessDeniedException);
                    }).authenticationEntryPoint((request, response, authException) -> {
                        log.error("===============================authException ", authException);
                    });
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .apply(authorizationServerConfigurer);

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
                                                           RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/oauth2/token")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(customizer -> {
                    customizer.accessDeniedHandler((request, response, accessDeniedException) -> {
                        log.error("===============================accessDeniedException ", accessDeniedException);
                    }).authenticationEntryPoint((request, response, authException) -> {
                        log.error("===============================authException ", authException);
                    });
                })
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
//        KeyPair keyPair = generateRsaKey();
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) new RSA(null, this.publicKey).getPublicKey())
                .privateKey((RSAPrivateKey) new RSA(this.privateKey, null).getPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                // JWT 构建器
                JwtClaimsSet.Builder claims = context.getClaims();
                // 用户认证
                Authentication principal = context.getPrincipal();
                claims.id(IdUtil.fastUUID());
                // 用户权限
                Set<String> authorities = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                authorities.addAll(context.getAuthorizedScopes());
                claims.claim("authorities", authorities);

                if (principal instanceof CloudLeafAdminUserAuthenticationToken token) {
                    claims.claim("userId", token.getUserId());
                }

            }
        });
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();

        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println("privateKey : [" + KeyUtil.toBase64(keyPair.getPrivate()) + "]");
            System.out.println("publicKey : [" + KeyUtil.toBase64(keyPair.getPublic()) + "]");
            System.out.println("-------------------------------------------------------------------------------------------");
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
}


