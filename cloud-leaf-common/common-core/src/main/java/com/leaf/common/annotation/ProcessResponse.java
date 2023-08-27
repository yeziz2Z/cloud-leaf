package com.leaf.common.annotation;

import com.leaf.common.enums.ProcessResponseType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProcessResponse {

    /**
     * 结果处理类型
     *
     * @return {@code ProcessResponseType}
     */
    ProcessResponseType processResponseType();
}
