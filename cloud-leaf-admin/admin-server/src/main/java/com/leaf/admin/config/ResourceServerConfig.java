package com.leaf.admin.config;

import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class ResourceServerConfig {

    @Value("${cloud.jwk.public-key}")
    private String publicKey;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/loadByUsername")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(jwtConfigurer -> {
                    RSAPublicKey rsaPublicKey = (RSAPublicKey) new RSA(null, this.publicKey).getPublicKey();
                    NimbusJwtDecoder.PublicKeyJwtDecoderBuilder publicKeyJwtDecoderBuilder = NimbusJwtDecoder
                            .withPublicKey(rsaPublicKey);
                    NimbusJwtDecoder nimbusJwtDecoder = publicKeyJwtDecoderBuilder.build();
                    jwtConfigurer.decoder(nimbusJwtDecoder);
                }));
        return http.build();
    }
}
