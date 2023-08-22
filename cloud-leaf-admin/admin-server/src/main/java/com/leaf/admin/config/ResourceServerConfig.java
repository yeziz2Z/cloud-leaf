package com.leaf.admin.config;

import cn.hutool.crypto.asymmetric.RSA;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaf.common.result.Result;
import com.leaf.common.result.ResultCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

/**
 * @author liuk
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@Slf4j
@EnableMethodSecurity
@RequiredArgsConstructor
public class ResourceServerConfig {

    @Value("${cloud.jwk.public-key}")
    private String publicKey;

    private final RedisTemplate<String, Object> redisTemplate;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/captcha", "/auth/getUserAuthorities")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwtConfigurer -> {
                            RSAPublicKey rsaPublicKey = (RSAPublicKey) new RSA(null, this.publicKey).getPublicKey();
                            NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder
                                    .withPublicKey(rsaPublicKey)
                                    /*.jwtProcessorCustomizer(jwtProcessor -> {
                                        jwtProcessor.setJWTClaimsSetVerifier(((jwtClaimsSet, securityContext) -> {
                                            log.info("jwtClaimsSet ：{}", jwtClaimsSet);
                                        }));
                                    })*/;
                            NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
                            nimbusJwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                                    new JwtTimestampValidator(),
                                    token -> {
                                        String tokenId = token.getId();
                                        if (Objects.nonNull(redisTemplate.opsForValue().get(tokenId))) {
                                            return OAuth2TokenValidatorResult.failure(
                                                    new OAuth2Error(String.valueOf(ResultCode.TOKEN_ACCESS_FORBIDDEN.getCode()))
                                            );
                                        }
                                        return OAuth2TokenValidatorResult.success();
                                    }));
                            jwtConfigurer.decoder(nimbusJwtDecoder);

                        })
                        // token 无效自定义异常
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.error("token error：", authException);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            ObjectMapper mapper = new ObjectMapper();
                            ResultCode tokenResultCode = ResultCode.INVALID_TOKEN;
                            if (authException.getMessage().indexOf("Jwt expired") != -1){
                                tokenResultCode = ResultCode.TOKEN_EXPIRED;
                            }
                            if (authException.getMessage().indexOf(String.valueOf(ResultCode.TOKEN_ACCESS_FORBIDDEN.getCode())) != -1){
                                tokenResultCode = ResultCode.TOKEN_ACCESS_FORBIDDEN;
                            }
                            mapper.writeValue(response.getOutputStream(), Result.result(tokenResultCode));
                        })
                        // 自定义无权限异常
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(response.getOutputStream(), Result.result(ResultCode.ACCESS_UNAUTHORIZED));
                        }));
        return http.build();
    }
}
