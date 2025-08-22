package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.ValidEmail;

import java.util.regex.Pattern;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private boolean allowInternationalDomains;
    private boolean requireTLD;
    private static final String EMAIL_PATTERN =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final String STRICT_EMAIL_PATTERN =
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern STRICT_EMAIL_REGEX = Pattern.compile(STRICT_EMAIL_PATTERN);

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.allowInternationalDomains = constraintAnnotation.allowInternationalDomains();
        this.requireTLD = constraintAnnotation.requireTLD();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true;
        }

        email = email.trim().toLowerCase();
        if (!STRICT_EMAIL_REGEX.matcher(email).matches()) {
            return false;
        }
        if (!isValidEmailStructure(email)) {
            return false;
        }
        if (hasInvalidPatterns(email)) {
            return false;
        }

        return true;
    }

    private boolean isValidEmailStructure(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        if (localPart.length() == 0 || localPart.length() > 64) {
            return false;
        }

        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            return false;
        }

        if (localPart.contains("..")) {
            return false;
        }

        if (domainPart.length() == 0 || domainPart.length() > 255) {
            return false;
        }

        if (domainPart.startsWith(".") || domainPart.endsWith(".") ||
            domainPart.startsWith("-") || domainPart.endsWith("-")) {
            return false;
        }

        if (requireTLD && !domainPart.contains(".")) {
            return false;
        }

        return true;
    }

    private boolean hasInvalidPatterns(String email) {
        String[] invalidPatterns = {
            "@.",
            ".@",
            "@@",
            "..",
            " "
        };

        for (String pattern : invalidPatterns) {
            if (email.contains(pattern)) {
                return true;
            }
        }

        return false;
    }
}
