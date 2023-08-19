package com.leaf.common.web.advice;

import cn.hutool.crypto.CryptoException;
import com.leaf.common.annotation.DecryptField;
import com.leaf.common.exception.BusinessException;
import com.leaf.common.result.ResultCode;
import com.leaf.common.utils.ReflectionUtils;
import com.leaf.common.web.utils.SecureUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 对请求参数标注 {@code DecryptField} 类型的参数进行解密处理
 */
@ControllerAdvice
@Slf4j
@ConditionalOnProperty(prefix = "cloud.leaf.request.decrypt", value = "enable", havingValue = "true")
@RequiredArgsConstructor
public class DecryptRequestBodyFieldAdvice extends RequestBodyAdviceAdapter {

    private final SecureUtils secureUtils;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (targetType instanceof Class<?> clazz) {
            return ReflectionUtils.hasAnnotationComplex(clazz, DecryptField.class, String.class);
        }
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            ReflectionUtils.setStringFieldComplex(body, (obj, field) -> {
                try {
                    field.set(obj, this.secureUtils.decode(field.get(obj).toString()));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (CryptoException exception) {
                    throw new BusinessException(ResultCode.INVALID_PASSWORD);
                }
            }, DecryptField.class);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return body;
    }


}
