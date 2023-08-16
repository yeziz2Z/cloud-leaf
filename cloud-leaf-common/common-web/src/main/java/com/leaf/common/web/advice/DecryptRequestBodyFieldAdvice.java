package com.leaf.common.web.advice;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.codec.Base64;
import com.leaf.common.exception.BusinessException;
import com.leaf.common.result.ResultCode;
import com.leaf.common.web.annotation.DecryptField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 对请求参数标注 {@code DecryptField} 类型的参数进行解密处理
 */
@ControllerAdvice
@Slf4j
public class DecryptRequestBodyFieldAdvice extends RequestBodyAdviceAdapter {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (targetType instanceof Class<?> clazz) {
            return hasAnnotation(clazz);
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
            setDecryptField(body, (Class<?>) targetType);
            return body;
        } catch (IllegalAccessException e) {
            log.error("DecodeRequestBodyAdvice transform decrypt param error", e);
            throw new BusinessException(ResultCode.DECODE_PARAM_ERROR);
        }
    }

    /**
     * 递归赋值解析后参数
     *
     * @param body  Java 对象
     * @param clazz 对象类型
     * @throws IllegalAccessException 使用反射 访问受限异常，
     *                                此处开始使用了 {@code field.setAccessible(true);}
     *                                不太可能出现该异常
     */
    private void setDecryptField(Object body, Class<?> clazz) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(body);
            // 空值 不需要做任何转换
            if (Objects.isNull(value)) {
                continue;
            }
            // 目标参数 设值
            if (field.getType().equals(String.class)
                    && AnnotationUtil.hasAnnotation(field, DecryptField.class)) {
                field.set(body, Base64.decodeStr(field.get(body).toString()));
            } else if (field.getType().isArray()) {// 数组类型属性
                for (Object obj : (Object[]) value) {
                    setDecryptField(obj, obj.getClass());
                }
            } else if (Collection.class.isAssignableFrom(field.getType())) {// 集合属性
                for (Object obj : (Collection<?>) value) {
                    setDecryptField(obj, obj.getClass());
                }
            } else if (Map.class.isAssignableFrom(field.getType())) {// map属性 仅处理 values ,不太可能有人脑子有坑 用自定义业务参数对象 用作map key
                Map<?, ?> map = (Map<?, ?>) value;
                for (Object obj : map.values()) {
                    setDecryptField(obj, obj.getClass());
                }
            } else if (field.getType().getName().startsWith("java.")) {// 系统类库不做处理

            } else { // 自定义对象
                setDecryptField(value, field.getType());
            }
        }
    }


    /**
     * 递归判断是否有 String 类型属性添加了 需要解密的注解 {@code @DecryptField}
     *
     * @param clazz 类对象
     */
    private boolean hasAnnotation(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // String 类型 且标有注解
            if (field.getType().equals(String.class) && AnnotationUtil.hasAnnotation(field, DecryptField.class)) {
                return true;
            }
            // 数组
            if (field.getType().isArray()) {
                return hasAnnotation(field.getType().getComponentType());
            }
            // 集合
            if (Collection.class.isAssignableFrom(field.getType()) &&
                    field.getGenericType() instanceof ParameterizedType parameterizedType
                    && parameterizedType.getActualTypeArguments().length > 0
                    && parameterizedType.getActualTypeArguments()[0] instanceof Class<?> clz) {
                return hasAnnotation(clz);
            }
            // Map
            if (Map.class.isAssignableFrom(field.getType()) &&
                    field.getGenericType() instanceof ParameterizedType parameterizedType
                    && parameterizedType.getActualTypeArguments().length > 1
                    /**
                     *  Map<Key, Value> parameterizedType.getActualTypeArguments()[1] 代表第二个泛型定义
                     *  传参使用 开发中不推荐使用Map 定义参数，为保证可用也对map 进行了验证
                     */
                    && parameterizedType.getActualTypeArguments()[1] instanceof Class<?> clz) {
                return hasAnnotation(clz);
            }
            // java 类库
            if (field.getType().getName().startsWith("java.")) {
                continue;
            }
            // 自定义对象 递归查询
            return hasAnnotation(field.getType());
        }
        return false;
    }
}
