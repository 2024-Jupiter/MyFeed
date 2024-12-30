package com.myfeed.annotation;

import com.myfeed.annotation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.TYPE)
@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatch {
    String message() default "비밀번호가 일치하지 않습니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
