package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator.UniqueEmailValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {
    String message() default "Email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
