package com.leaf.gateway.route;

import com.leaf.gateway.handler.CaptchaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CaptchaRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(CaptchaHandler captchaHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/captcha")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
                captchaHandler);
    }
}
