package com.leaf.common.web.advice;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.leaf.common.annotation.DesensitizedField;
import com.leaf.common.annotation.ProcessResponse;
import com.leaf.common.enums.ProcessResponseType;
import com.leaf.common.result.Result;
import com.leaf.common.utils.ReflectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 对返回参数标注 {@code DesensitizedField} 类型的参数进行脱敏处理
 */
@ControllerAdvice
@ConditionalOnProperty(prefix = "cloud.leaf.response.desensitized", value = "enable", havingValue = "true")
public class DesensitizedResponseBodyFieldAdvice implements ResponseBodyAdvice, Ordered {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        ProcessResponse methodAnnotation = returnType.getMethodAnnotation(ProcessResponse.class);
        return Objects.nonNull(methodAnnotation) && Objects.equals(methodAnnotation.processResponseType(), ProcessResponseType.DESENSITIZATION);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Result<?> result && Objects.nonNull(result.getData())) {
            try {
                ReflectionUtils.setStringFieldComplex(result.getData(), (obj, field) -> {
                    DesensitizedField annotation = AnnotationUtil.getAnnotation(field, DesensitizedField.class);
                    DesensitizedUtil.DesensitizedType type = annotation.desensitizedType();
                    try {
                        field.set(obj, DesensitizedUtil.desensitized(field.get(obj).toString(), type));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }, DesensitizedField.class);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return body;

    }


}
