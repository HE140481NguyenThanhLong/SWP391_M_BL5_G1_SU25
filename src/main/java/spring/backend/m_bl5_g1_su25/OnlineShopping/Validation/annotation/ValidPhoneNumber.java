package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator.PhoneNumberValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
@Documented
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String pattern() default "^[0-9]{10,11}$"; // Default Vietnamese phone format
}
