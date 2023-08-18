package com.leaf.common.utils;

import cn.hutool.core.annotation.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.lang.module.ModuleDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * 反射工具类
 */
public class ReflectionUtils {

    /**
     * 获取 clazz fieldClazz 类型属性是否标注了 {@code annoClazz} 注解，可递归调用内置复杂对象支持
     *
     * @param clazz      目标对象
     * @param annoClazz  注解
     * @param fieldClazz 标注注解属性
     * @param recursion  是否递归调用
     * @return
     */

    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annoClazz, Class<?> fieldClazz, boolean recursion) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            // String 类型 且标有注解
            if (field.getType().equals(fieldClazz) && AnnotationUtil.hasAnnotation(field, annoClazz)) {
                return true;
            }
            if (recursion) {
                // 数组
                if (field.getType().isArray()) {
                    return hasAnnotation(field.getType().getComponentType(), annoClazz, fieldClazz, recursion);
                }
                // 集合
                if (Collection.class.isAssignableFrom(field.getType()) &&
                        field.getGenericType() instanceof ParameterizedType parameterizedType
                        && parameterizedType.getActualTypeArguments().length > 0
                        && parameterizedType.getActualTypeArguments()[0] instanceof Class<?> clz) {
                    return hasAnnotation(clz, annoClazz, fieldClazz, true);
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
                    return hasAnnotation(clz, annoClazz, fieldClazz, true);
                }
                // java 类库
                if (field.getType().isPrimitive() || field.getType().getName().startsWith("java.")) {
                    continue;
                }
                // 自定义对象 递归查询
                return hasAnnotation(field.getType(), annoClazz, fieldClazz, true);
            }
        }
        return false;
    }

    public static boolean hasAnnotationSimple(Class<?> clazz, Class<? extends Annotation> annoClazz, Class<?> fieldClazz) {
        return hasAnnotation(clazz, annoClazz, fieldClazz, false);
    }

    public static boolean hasAnnotationComplex(Class<?> clazz, Class<? extends Annotation> annoClazz, Class<?> fieldClazz) {
        return hasAnnotation(clazz, annoClazz, fieldClazz, true);
    }

    public static void setFieldSimple(Object body, BiConsumer<? super Object, Field> consumer, Class<? extends Annotation> annoClazz, Class<?> fieldClazz) throws IllegalAccessException {
        setFieldWithRecursive(body, consumer, annoClazz, fieldClazz, false);
    }

    public static void setFieldComplex(Object body, BiConsumer<? super Object, Field> consumer, Class<? extends Annotation> annoClazz, Class<?> fieldClazz) throws IllegalAccessException {
        setFieldWithRecursive(body, consumer, annoClazz, fieldClazz, true);
    }

    public static void setStringFieldComplex(Object body, BiConsumer<? super Object, Field> consumer, Class<? extends Annotation> annoClazz) throws IllegalAccessException {
        setFieldWithRecursive(body, consumer, annoClazz, String.class, true);
    }

    public static void setFieldWithRecursive(Object body, BiConsumer<? super Object, Field> consumer, Class<? extends Annotation> annoClazz, Class<?> fieldClazz, boolean isRecursion) throws IllegalAccessException {
        if (Objects.isNull(body)) {
            return;
        }
        Field[] fields = body.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (isFieldAccessible(field, body.getClass())) {
                continue;
            }
            field.setAccessible(true);
            Object value = field.get(body);
            // 空值 或者 基本类型参数 不需要做任何转换直接跳过
            if (Objects.isNull(value) || field.getType().isPrimitive()) {
                continue;
            }
            // 目标参数 设值逻辑处理
            if (field.getType().equals(fieldClazz)
                    && AnnotationUtil.hasAnnotation(field, annoClazz)) {
                consumer.accept(body, field);
                continue;
            }
            if (isRecursion) {
                if (field.getType().isArray()) {// 数组类型属性
                    for (Object obj : (Object[]) value) {
                        setFieldWithRecursive(obj, consumer, annoClazz, fieldClazz, true);
                    }
                } else if (Collection.class.isAssignableFrom(field.getType())) {// 集合属性
                    for (Object obj : (Collection<?>) value) {
                        setFieldWithRecursive(obj, consumer, annoClazz, fieldClazz, true);
                    }
                } else if (Map.class.isAssignableFrom(field.getType())) {// map属性 仅处理 values ,不太可能有人脑子有坑 用自定义业务参数对象 用作map key
                    Map<?, ?> map = (Map<?, ?>) value;
                    for (Object obj : map.values()) {
                        setFieldWithRecursive(obj, consumer, annoClazz, fieldClazz, true);
                    }
                } else if (field.getType().getName().startsWith("java.")) {// 系统类库不做处理

                } else { // 自定义对象
                    setFieldWithRecursive(value, consumer, annoClazz, fieldClazz, true);
                }
            }

        }
    }


    private static boolean isFieldAccessible(Field field, Class<?> clazz) {
        int modifiers = field.getModifiers();
        // 静态属性 或者 final修饰 或者 transient
        if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers) || Modifier.isTransient(modifiers)) {
            return true;
        }
        Module module = clazz.getModule();
        ModuleLayer layer = module.getLayer();
        ModuleDescriptor descriptor = module.getDescriptor();
        if (Objects.isNull(descriptor)) {
            return false;
        }

        boolean isOpenedToAll = descriptor.isOpen();
        boolean isOpenedToUnnamed = layer.findModule("java.base").isPresent();

        return isOpenedToAll || isOpenedToUnnamed;
    }

}
