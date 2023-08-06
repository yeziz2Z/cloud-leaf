package com.leaf.common.validation.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

/**
 * @author liuk
 */
@Configuration
public class ValidationConfiguration {

    /**
     * 自定义validator实现快速失败
     */
    @Bean
    public Validator validator(AutowireCapableBeanFactory autowireCapableBeanFactory) {
        try (ValidatorFactory factory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true) // failFast=true 不校验所有参数，只要出现校验失败情况直接返回，不再进行后续参数校验
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
                .buildValidatorFactory()) {
            return factory.getValidator();
        }
    }
}
