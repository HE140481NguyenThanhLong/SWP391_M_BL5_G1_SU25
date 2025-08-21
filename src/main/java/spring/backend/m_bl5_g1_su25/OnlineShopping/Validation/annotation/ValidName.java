package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator.ValidNameValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidNameValidator.class)
@Documented
public @interface ValidName {
    String message() default "Name must contain only letters, spaces, hyphens, and apostrophes";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean allowSpaces() default true;
    boolean allowHyphens() default true;
    boolean allowApostrophes() default true;
    int minLength() default 2;
    int maxLength() default 50;
}
