package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator.PasswordStrengthValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Documented
public @interface PasswordStrength {
    String message() default "Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int minLength() default 8;
    boolean requireUppercase() default true;
    boolean requireLowercase() default true;
    boolean requireDigit() default true;
    boolean requireSpecialChar() default false;
}
