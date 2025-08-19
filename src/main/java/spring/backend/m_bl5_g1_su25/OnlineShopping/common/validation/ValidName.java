package spring.backend.m_bl5_g1_su25.OnlineShopping.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NameValidator.class)
@Documented
public @interface ValidName {
    String message() default "Name must contain only letters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
