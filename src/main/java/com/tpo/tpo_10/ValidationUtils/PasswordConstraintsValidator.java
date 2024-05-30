package com.tpo.tpo_10.ValidationUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class PasswordConstraintsValidator implements ConstraintValidator<PasswordConstraints, String> {

    @Autowired
    private MessageSource messageSource;
    @Override
    public void initialize(PasswordConstraints constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }
        int lowerCaseCount = (int) password.chars().filter(Character::isLowerCase).count();
        int upperCaseCount = (int) password.chars().filter(Character::isUpperCase).count();
        int digitCount = (int) password.chars().filter(Character::isDigit).count();
        int specialCharCount = (int) password.chars().filter(ch -> !Character.isLetterOrDigit(ch)).count();

        context.disableDefaultConstraintViolation();

        if (lowerCaseCount < 1) {
            String message = messageSource.getMessage("error.password.lowercase", null, LocaleContextHolder.getLocale());
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        if (upperCaseCount < 2) {
            String message = messageSource.getMessage("error.password.uppercase", null, LocaleContextHolder.getLocale());
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        if (digitCount < 3) {
            String message = messageSource.getMessage("error.password.digit", null, LocaleContextHolder.getLocale());
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        if (specialCharCount < 4) {
            String message = messageSource.getMessage("error.password.specialchar", null, LocaleContextHolder.getLocale());
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        if (password.length() < 10) {
            String message = messageSource.getMessage("error.password.length", null, LocaleContextHolder.getLocale());
            context.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        return lowerCaseCount >= 1 && upperCaseCount >= 2 && digitCount >= 3 && specialCharCount >= 4 && password.length() >= 10;
    }
}
