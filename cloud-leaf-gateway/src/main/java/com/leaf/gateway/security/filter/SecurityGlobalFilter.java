package com.leaf.gateway.security.filter;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.leaf.common.constant.SecurityConstant;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * 安全拦截全局过滤器
 * 转发请求时  将 jwt payload 信息写入请求头
 *
 * @date 2022年6月3日
 */
@Component
@Slf4j
@Order(value = 0)
public class SecurityGlobalFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        // 非JWT放行不做后续解析处理
        String token = request.getHeaders().getFirst(SecurityConstant.AUTHORIZATION_KEY);
        if (StrUtil.isBlank(token) || !StrUtil.startWithIgnoreCase(token, SecurityConstant.JWT_PREFIX)) {
            return chain.filter(exchange);
        }

        try {
            // token 会写至 request 请求头
            token = StrUtil.replaceIgnoreCase(token, SecurityConstant.JWT_PREFIX, Strings.EMPTY);

            String payload = StrUtil.toString(JWSObject.parse(token).getPayload());
            request = exchange.getRequest().mutate()
                    .header(SecurityConstant.JWT_PAYLOAD_KEY, URLEncoder.encode(payload, StandardCharsets.UTF_8.name()))
                    .build();
            exchange = exchange.mutate().request(request).build();
        } catch (ParseException | UnsupportedEncodingException e) {
            ExceptionUtil.wrapAndThrow(e);
        }
        return chain.filter(exchange);
    }

}
