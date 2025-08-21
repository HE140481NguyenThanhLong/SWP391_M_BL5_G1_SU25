package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator.ValidEmailValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default "Please provide a valid email address";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean allowInternationalDomains() default true;
    boolean requireTLD() default true;
}
