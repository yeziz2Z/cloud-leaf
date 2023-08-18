package com.leaf.common.web.advice;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对返回参数标注 {@code EncryptField} 类型的参数进行加密处理
 */
@ControllerAdvice
@ConditionalOnProperty(prefix = "cloud.leaf.response.encrypt", value = "enable", havingValue = "true")
public class EncryptResponseBodyFieldAdvice implements ResponseBodyAdvice, Ordered {
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        /*try {
            setEncryptField(body instanceof Result<?> result ? result.getData() : body);
            return body;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/
        return body;

    }


}
