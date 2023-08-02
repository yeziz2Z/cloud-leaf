package com.leaf.admin.annotation;


import com.leaf.common.enums.BusinessTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 模块
     *
     */
    String module() default "";

    /**
     * 操作类型
     */
    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;
}
