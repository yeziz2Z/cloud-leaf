///*
//package com.leaf.gateway.security.config;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.extra.spring.SpringUtil;
//import com.leaf.common.constant.SecurityConstant;
//import com.leaf.gateway.enums.UserAuthErrorEnum;
//import com.leaf.gateway.utlis.ResponseUtil;
//import com.nimbusds.jwt.proc.BadJWTException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
//import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
//import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
//import reactor.core.publisher.Mono;
//
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//
//@Configuration
//@EnableWebFluxSecurity
//@Slf4j
//public class ResourceServerConfig {
//
//    @Autowired
//    ResourceServerManager resourceServerManager;
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//
//        http
//                .oauth2ResourceServer()
//                .jwt()
//                .jwtAuthenticationConverter(jwtAuthenticationConverter())
//                // jwt 解码以及验证
//                .jwtDecoder(jwtDecoder())
//                .and()
//                // jwt 失效或者无效处理
//                .authenticationEntryPoint(authenticationEntryPoint())
//                .and()
//                .authorizeExchange()
//                .pathMatchers("/cloud-leaf-oauth2/oauth/token/**", "/captcha")
//                .permitAll()
//                .anyExchange()
//                .access(resourceServerManager)
//                .and()
//                .exceptionHandling()
//                // 权限不足
//                .accessDeniedHandler(accessDeniedHandler())
//                // 处理未授权
//                .authenticationEntryPoint(authenticationEntryPoint())
//                .and()
//                .csrf()
//                .disable();
//
//        return http.build();
//    }
//
//
//    @Bean
//    public ReactiveJwtDecoder jwtDecoder() {
//        SecretKey secretKey = new SecretKeySpec("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJkZXB0SWQiOjIsImV4cCI6MTY1MzAzNTYxOSwidXNlcklkIjoyLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiI1ZGFhZDljZi04YzhiLTQ5NzItYjBiYi1lYjdiZjc3YjAxNWIiLCJjbGllbnRfaWQiOiJtYWxsLWFkbWluLXdlYiIsInVzZXJuYW1lIjoiYWRtaW4ifQ.YKu2Z8ZEBIVqJHdJhKpZvwg1k3BdsCe0g2LxI8iHrtg".getBytes(), "HMACSHA256");
//        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder
//                .withSecretKey(secretKey)
//                .macAlgorithm(MacAlgorithm.HS256)
//                .jwtProcessorCustomizer(jwtProcessor -> {
//                    jwtProcessor.setJWTClaimsSetVerifier(((claimsSet, context) -> {
//                        if (claimsSet.getClaims().containsKey(SecurityConstant.JWT_ATI)) {
//                            throw new BadJWTException("非法的AccessToken");
//                        }
//                        if (SpringUtil.getBean(StringRedisTemplate.class).hasKey(SecurityConstant.TOKEN_BLACKLIST_PREFIX + claimsSet.getJWTID())) {
//                            throw new BadJWTException("token已被禁止访问");
//                        }
//                    }));
//                })
//                .build();
////        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(Arrays.asList(new JwtTimestampValidator(), new JwtValidator(SpringUtil.getBean(StringRedisTemplate.class)))));
//        return jwtDecoder;
//    }
//
//
//    */
///**
//     * @link https://blog.csdn.net/qq_24230139/article/details/105091273
//     * ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication
//     * 需要把jwt的Claim中的authorities加入
//     * 方案：重新定义权限管理器，默认转换器JwtGrantedAuthoritiesConverter
//     * <p>
//     * 当前解决方案 不解析角色  根据用户id从缓存查找完整权限
//     *//*
//
//    @Bean
//    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
//        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
//        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
//
//
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
//
//        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
//    }
//
//
//    */
///**
//     * 处理权限不足
//     *
//     * @return
//     *//*
//
//    @Bean
//    ServerAccessDeniedHandler accessDeniedHandler() {
//        return (exchange, denied) -> {
//            log.error("err", denied);
//            return Mono.defer(() -> Mono.just(exchange.getResponse()))
//                    .flatMap(response -> ResponseUtil.writeErrorInfo(response,
//                            UserAuthErrorEnum.ACCESS_DENY_ERROR,
//                            HttpStatus.FORBIDDEN));
//        };
//
//    }
//
//    */
///**
//     * token 无效&过期  处理
//     *
//     * @return
//     *//*
//
//    @Bean
//    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
//        return (exchange, denied) -> {
//            log.info("===================== msg {}", denied);
//            return Mono.defer(() -> Mono.just(exchange.getResponse()))
//                    .flatMap(response -> ResponseUtil.writeErrorInfo(response,
//                            // token过期
//                            StrUtil.startWith(denied.getMessage(), "Jwt expired") ? UserAuthErrorEnum.ACCESS_TOKEN_EXPIRED : UserAuthErrorEnum.ACCESS_TOKEN_INVALID,
//                            HttpStatus.UNAUTHORIZED)
//                    );
//        };
//
//
//    }
//}
//*/
