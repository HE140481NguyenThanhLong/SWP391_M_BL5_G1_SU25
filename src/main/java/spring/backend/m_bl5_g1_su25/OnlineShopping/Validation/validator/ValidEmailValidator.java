package spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import spring.backend.m_bl5_g1_su25.OnlineShopping.Validation.annotation.ValidEmail;

import java.util.regex.Pattern;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private boolean allowInternationalDomains;
    private boolean requireTLD;

    // RFC 5322 compliant email regex pattern
    private static final String EMAIL_PATTERN =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // More strict pattern for common domains
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
            return true; // Let @NotBlank handle empty validation
        }

        email = email.trim().toLowerCase();

        // Basic format validation
        if (!STRICT_EMAIL_REGEX.matcher(email).matches()) {
            return false;
        }

        // Additional validations
        if (!isValidEmailStructure(email)) {
            return false;
        }

        // Check for common invalid patterns
        if (hasInvalidPatterns(email)) {
            return false;
        }

        return true;
    }

    private boolean isValidEmailStructure(String email) {
        // Split email into local and domain parts
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        // Validate local part (before @)
        if (localPart.length() == 0 || localPart.length() > 64) {
            return false;
        }

        // Local part cannot start or end with a dot
        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            return false;
        }

        // Local part cannot have consecutive dots
        if (localPart.contains("..")) {
            return false;
        }

        // Validate domain part (after @)
        if (domainPart.length() == 0 || domainPart.length() > 255) {
            return false;
        }

        // Domain cannot start or end with a dot or hyphen
        if (domainPart.startsWith(".") || domainPart.endsWith(".") ||
            domainPart.startsWith("-") || domainPart.endsWith("-")) {
            return false;
        }

        // Check if domain has at least one dot (TLD requirement)
        if (requireTLD && !domainPart.contains(".")) {
            return false;
        }

        return true;
    }

    private boolean hasInvalidPatterns(String email) {
        // Check for common invalid email patterns
        String[] invalidPatterns = {
            "@.",       // @.domain
            ".@",       // local.@domain
            "@@",       // double @
            "..",       // consecutive dots
            " "         // spaces
        };

        for (String pattern : invalidPatterns) {
            if (email.contains(pattern)) {
                return true;
            }
        }

        return false;
    }
}
