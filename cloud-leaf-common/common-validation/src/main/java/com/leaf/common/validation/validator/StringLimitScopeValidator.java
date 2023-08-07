package com.leaf.common.validation.validator;

import com.leaf.common.validation.constraints.StringLimitScope;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author liuk
 */
public class StringLimitScopeValidator implements ConstraintValidator<StringLimitScope, String> {

    private Set<String> set;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return this.set.contains(value);
    }

    @Override
    public void initialize(StringLimitScope constraintAnnotation) {
        this.set = Arrays.stream(constraintAnnotation.values()).collect(Collectors.toSet());
    }
}
