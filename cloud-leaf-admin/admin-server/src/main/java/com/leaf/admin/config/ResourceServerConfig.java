package com.leaf.admin.config;

import cn.hutool.crypto.asymmetric.RSA;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leaf.common.result.Result;
import com.leaf.common.result.ResultCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;

/**
 * @author liuk
 */
@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
@Slf4j
@EnableMethodSecurity
public class ResourceServerConfig {

    @Value("${cloud.jwk.public-key}")
    private String publicKey;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwtConfigurer -> {
                            RSAPublicKey rsaPublicKey = (RSAPublicKey) new RSA(null, this.publicKey).getPublicKey();
                            NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder
                                    .withPublicKey(rsaPublicKey)
                                    .jwtProcessorCustomizer(jwtProcessor -> {
                                        jwtProcessor.setJWTClaimsSetVerifier(((jwtClaimsSet, securityContext) -> {
                                            log.info("jwtClaimsSet ：{}", jwtClaimsSet);
                                        }));
                                    });
                            NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
                            jwtConfigurer.decoder(nimbusJwtDecoder);

                        })
                        // token 无效自定义异常
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.writeValue(response.getOutputStream(), Result.result(ResultCode.INVALID_TOKEN));
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
