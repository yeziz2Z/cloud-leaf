package com.leaf.common.annotation;

import com.leaf.common.enums.BusinessTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 模块
     *
     * @return
     */
    String module() default "";

    /**
     * 操作类型
     * @return
     */
    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;
}
