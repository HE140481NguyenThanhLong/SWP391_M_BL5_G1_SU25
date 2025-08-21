package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.PasswordStrength;

import java.util.regex.Pattern;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {

    private int minLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigit;
    private boolean requireSpecialChar;

    @Override
    public void initialize(PasswordStrength constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireUppercase = constraintAnnotation.requireUppercase();
        this.requireLowercase = constraintAnnotation.requireLowercase();
        this.requireDigit = constraintAnnotation.requireDigit();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true; // Let @NotBlank handle null validation
        }

        // Check minimum length
        if (password.length() < minLength) {
            return false;
        }

        // Check uppercase requirement
        if (requireUppercase && !Pattern.matches(".*[A-Z].*", password)) {
            return false;
        }

        // Check lowercase requirement
        if (requireLowercase && !Pattern.matches(".*[a-z].*", password)) {
            return false;
        }

        // Check digit requirement
        if (requireDigit && !Pattern.matches(".*\\d.*", password)) {
            return false;
        }

        // Check special character requirement
        if (requireSpecialChar && !Pattern.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", password)) {
            return false;
        }

        return true;
    }
}
