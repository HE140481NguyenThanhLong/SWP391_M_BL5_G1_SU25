package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.ValidName;

import java.util.regex.Pattern;

public class ValidNameValidator implements ConstraintValidator<ValidName, String> {

    private ValidName validName;
    private Pattern pattern;

    @Override
    public void initialize(ValidName validName) {
        this.validName = validName;

        // Build regex pattern based on annotation parameters
        StringBuilder regexBuilder = new StringBuilder("^[a-zA-ZÀ-ÿĀ-žА-я");

        if (validName.allowSpaces()) {
            regexBuilder.append("\\s");
        }

        if (validName.allowHyphens()) {
            regexBuilder.append("\\-");
        }

        if (validName.allowApostrophes()) {
            regexBuilder.append("'");
        }

        regexBuilder.append("]{")
                   .append(validName.minLength())
                   .append(",")
                   .append(validName.maxLength())
                   .append("}$");

        this.pattern = Pattern.compile(regexBuilder.toString());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }

        String trimmedValue = value.trim();

        // Check length constraints
        if (trimmedValue.length() < validName.minLength() ||
            trimmedValue.length() > validName.maxLength()) {
            return false;
        }

        // Check against pattern
        if (!pattern.matcher(trimmedValue).matches()) {
            return false;
        }

        // Additional validations
        // No leading or trailing spaces after trim
        if (!trimmedValue.equals(value.trim())) {
            return false;
        }

        // No consecutive spaces
        if (validName.allowSpaces() && trimmedValue.contains("  ")) {
            return false;
        }

        // No starting or ending with special characters
        char firstChar = trimmedValue.charAt(0);
        char lastChar = trimmedValue.charAt(trimmedValue.length() - 1);

        if (!Character.isLetter(firstChar) || !Character.isLetter(lastChar)) {
            return false;
        }

        return true;
    }
}
