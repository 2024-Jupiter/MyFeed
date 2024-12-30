package com.myfeed.annotation.validator;

import com.myfeed.annotation.PasswordMatch;
import com.myfeed.model.user.RegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordMatch, RegisterDto> {
    @Override
    public boolean isValid(RegisterDto dto, ConstraintValidatorContext constraintValidatorContext) {
        return dto.getPwd().equals(dto.getPwd2());
    }
}
