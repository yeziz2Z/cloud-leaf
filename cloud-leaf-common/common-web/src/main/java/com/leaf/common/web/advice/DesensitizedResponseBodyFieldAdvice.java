package com.leaf.common.web.advice;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.DesensitizedUtil;
import com.leaf.common.result.Result;
import com.leaf.common.web.annotation.DesensitizedField;
import com.leaf.common.web.annotation.EncryptField;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 对返回参数标注 {@code DesensitizedField} 类型的参数进行脱敏处理
 */
@ControllerAdvice
public class DesensitizedResponseBodyFieldAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            setDesensitizedField(body instanceof Result<?> result ? result.getData() : body);
            return body;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 递归赋值解析后参数
     *
     * @param body Java 对象
     * @throws IllegalAccessException 使用反射 访问受限异常，
     *                                此处开始使用了 {@code field.setAccessible(true);}
     *                                不太可能出现该异常
     */
    private void setDesensitizedField(Object body) throws IllegalAccessException {
        if (Objects.isNull(body)) {
            return;
        }
        Field[] fields = body.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(body);
            // 空值 不需要做任何转换
            if (Objects.isNull(value)) {
                continue;
            }
            // 目标参数 设值
            if (field.getType().equals(String.class)
                    && AnnotationUtil.hasAnnotation(field, DesensitizedField.class)) {
                DesensitizedField annotation = field.getAnnotation(DesensitizedField.class);
                field.set(body, DesensitizedUtil.desensitized(field.get(body).toString(), annotation.type()));
            } else if (field.getType().isArray()) {// 数组类型属性
                for (Object obj : (Object[]) value) {
                    setDesensitizedField(obj);
                }
            } else if (Collection.class.isAssignableFrom(field.getType())) {// 集合属性
                for (Object obj : (Collection<?>) value) {
                    setDesensitizedField(obj);
                }
            } else if (Map.class.isAssignableFrom(field.getType())) {// map属性 仅处理 values ,不太可能有人脑子有坑 用自定义业务参数对象 用作map key
                Map<?, ?> map = (Map<?, ?>) value;
                for (Object obj : map.values()) {
                    setDesensitizedField(obj);
                }
            } else if (field.getType().getName().startsWith("java.")) {// 系统类库不做处理

            } else { // 自定义对象
                setDesensitizedField(value);
            }
        }
    }
}
