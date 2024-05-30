package com.tpo.tpo_10.ValidationUtils;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = PasswordConstraintsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraints {
    String message() default "Password does not match required constraints";
    Class[] groups() default {};
    Class[] payload() default {};

}

