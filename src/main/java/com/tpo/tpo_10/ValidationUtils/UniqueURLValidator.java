package com.tpo.tpo_10.ValidationUtils;

import com.tpo.tpo_10.Repositories.LinkRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueURLValidator implements ConstraintValidator<UniqueURL, String> {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public void initialize(UniqueURL constraintAnnotation) {
    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        return url != null && !linkRepository.existsByTargetUrl(url);
    }
}