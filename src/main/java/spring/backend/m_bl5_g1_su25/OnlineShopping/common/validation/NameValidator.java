package spring.backend.m_bl5_g1_su25.OnlineShopping.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.ValidationUtils;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    @Override
    public void initialize(ValidName constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        ValidationUtils.ValidationResult result = ValidationUtils.validateName(name);

        if (!result.isSuccess()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(result.getErrorMessage())
                   .addConstraintViolation();
        }

        return result.isSuccess();
    }
}
