package com.leaf.common.web.annotation;

import cn.hutool.core.util.DesensitizedUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 字段脱敏
 *
 * @author liuk
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DesensitizedField {

    DesensitizedUtil.DesensitizedType desensitizedType();
}
