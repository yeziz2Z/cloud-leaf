package com.leaf.gateway.security.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.leaf.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class ResourceServerConfig {


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        http
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                // jwt 解码以及验证
                .jwtDecoder(jwtDecoder())
                .and()
                // jwt 失效或者无效处理
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authorizeExchange()
                .pathMatchers("/cloud-leaf-oauth2/oauth/token/**", "/captcha")
                .permitAll()
                .anyExchange()
                .access(resourceServerManager())
                .and()
                .exceptionHandling()
                // 权限不足
                .accessDeniedHandler(accessDeniedHandler())
                // 处理未授权
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .csrf()
                .disable();

        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJkZXB0SWQiOjIsImV4cCI6MTY1MzAzNTYxOSwidXNlcklkIjoyLCJhdXRob3JpdGllcyI6WyJBRE1JTiJdLCJqdGkiOiI1ZGFhZDljZi04YzhiLTQ5NzItYjBiYi1lYjdiZjc3YjAxNWIiLCJjbGllbnRfaWQiOiJtYWxsLWFkbWluLXdlYiIsInVzZXJuYW1lIjoiYWRtaW4ifQ.YKu2Z8ZEBIVqJHdJhKpZvwg1k3BdsCe0g2LxI8iHrtg".getBytes(), "HMACSHA256");
        return NimbusReactiveJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    /**
     * @link https://blog.csdn.net/qq_24230139/article/details/105091273
     * ServerHttpSecurity没有将jwt中authorities的负载部分当做Authentication
     * 需要把jwt的Claim中的authorities加入
     * 方案：重新定义权限管理器，默认转换器JwtGrantedAuthoritiesConverter
     */
    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }


    /**
     * 处理权限不足
     * @return
     */
    @Bean
    ServerAccessDeniedHandler accessDeniedHandler() {
        return (exchange, denied) ->
                Mono.defer(() -> Mono.just(exchange.getResponse()))
                        .flatMap(response -> {
                            response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                            response.getHeaders().set("Access-Control-Allow-Origin", "*");
                            response.getHeaders().set("Cache-Control", "no-cache");
                            response.setStatusCode(HttpStatus.FORBIDDEN);
                            String body = JSONUtil.toJsonStr(Result.fail("没有访问权限"));
                            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                            return response.writeWith(Mono.just(buffer))
                                    .doOnError(error -> DataBufferUtils.release(buffer));
                        });
    }

    /**
     * 鉴权管理器
     * @return
     */
    @Bean
    public ReactiveAuthorizationManager<AuthorizationContext> resourceServerManager() {
        return ((authentication, authorizationContext) -> {
            List<String> authorizedRoles = new ArrayList<>();
            authorizedRoles.add("admin");

            // 判断JWT中携带的用户角色是否有权限访问
            Mono<AuthorizationDecision> authorizationDecisionMono = authentication
                    .filter(Authentication::isAuthenticated)
                    .flatMapIterable(Authentication::getAuthorities)
                    .map(GrantedAuthority::getAuthority)
                    .any(authority -> {
                        String roleCode = authority.substring("ROLE_".length()); // 用户的角色
                        boolean hasAuthorized = CollectionUtil.isNotEmpty(authorizedRoles) && authorizedRoles.contains(roleCode);
                        return hasAuthorized;
                    })
                    .map(AuthorizationDecision::new)
                    .defaultIfEmpty(new AuthorizationDecision(false));
            return authorizationDecisionMono;

        });
    }

    /**
     * token 无效&过期  处理
     * @return
     */
    @Bean
    public ServerAuthenticationEntryPoint authenticationEntryPoint() {
        return (exchange, ex) -> {
            log.info(ex.getMessage(), ex);
            return Mono.defer(() -> Mono.just(exchange.getResponse()))
                    .flatMap(response -> {
                        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                        response.getHeaders().set("Access-Control-Allow-Origin", "*");
                        response.getHeaders().set("Cache-Control", "no-cache");
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        String body = JSONUtil.toJsonStr(Result.fail("token无效或已过期"));
                        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
                        return response.writeWith(Mono.just(buffer))
                                .doOnError(error -> DataBufferUtils.release(buffer));
                    });
        };

    }
}
