package com.leaf.common.validation.validator;

import com.leaf.common.validation.constraints.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author liuk
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return false;
    }

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}
