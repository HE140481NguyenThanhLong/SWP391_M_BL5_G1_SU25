package spring.backend.m_bl5_g1_su25.OnlineShopping.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import spring.backend.m_bl5_g1_su25.OnlineShopping.common.ValidationUtils;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        ValidationUtils.ValidationResult result = ValidationUtils.validatePhone(phone);

        if (!result.isSuccess()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(result.getErrorMessage())
                   .addConstraintViolation();
        }

        return result.isSuccess();
    }
}
