package com.leaf.config;

import cn.hutool.extra.spring.SpringUtil;
import feign.Contract;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuk
 */
@Configuration
@Slf4j
public class FeignConfig {
    @Bean
    public CloseableHttpClient httpClient5() {
        return HttpClientBuilder.create().addRequestInterceptorFirst((httpRequest, entityDetails, httpContext) -> {
            log.info("service : {}, feign request :{}, details: {}, httpContext: {}", SpringUtil.getApplicationName(), httpRequest, entityDetails, httpContext);
        }).build();
    }

    @Bean
    public Contract encoder() {
        return new SpringMvcContract();
    }
}
