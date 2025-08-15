package spring.backend.m_bl5_g1_su25.OnlineShopping.common;

import java.util.regex.Pattern;

/**
 * Utility class for common validation operations
 * Contains validation methods for numbers, text, unicode, and email formats
 */
public class ValidationUtils {

    // Regex patterns
    private static final Pattern NUMBERS_ONLY = Pattern.compile("^[0-9]+$");
    private static final Pattern LETTERS_ONLY = Pattern.compile("^[a-zA-Z\\s]+$");
    private static final Pattern LETTERS_WITH_UNICODE = Pattern.compile("^[\\p{L}\\p{M}\\s]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+\\-\\s()]+$");

    /**
     * Validate if string contains only numbers
     * @param input the string to validate
     * @return true if string contains only digits
     */
    public static boolean isNumbersOnly(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return NUMBERS_ONLY.matcher(input.trim()).matches();
    }

    /**
     * Validate if string contains only letters (English only)
     * @param input the string to validate
     * @return true if string contains only English letters and spaces
     */
    public static boolean isLettersOnly(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return LETTERS_ONLY.matcher(input.trim()).matches();
    }

    /**
     * Validate if string contains only letters with Unicode support (Vietnamese, etc.)
     * @param input the string to validate
     * @return true if string contains only letters (including Unicode) and spaces
     */
    public static boolean isLettersWithUnicode(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        return LETTERS_WITH_UNICODE.matcher(input.trim()).matches();
    }

    /**
     * Validate email format
     * @param email the email string to validate
     * @return true if email format is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format (numbers, +, -, spaces, parentheses)
     * @param phone the phone string to validate
     * @return true if phone format is valid
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String cleanPhone = phone.trim();
        return PHONE_PATTERN.matcher(cleanPhone).matches() &&
               cleanPhone.replaceAll("[^0-9]", "").length() >= 9; // At least 9 digits
    }

    /**
     * Validate string length within range
     * @param input the string to validate
     * @param minLength minimum length (inclusive)
     * @param maxLength maximum length (inclusive)
     * @return true if length is within range
     */
    public static boolean isValidLength(String input, int minLength, int maxLength) {
        if (input == null) {
            return false;
        }
        int length = input.trim().length();
        return length >= minLength && length <= maxLength;
    }

    /**
     * Check if string is null or empty after trimming
     * @param input the string to check
     * @return true if string is null or empty/whitespace only
     */
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Validate name format (letters with unicode support, length 2-100)
     * @param name the name to validate
     * @return ValidationResult with success status and error message
     */
    public static ValidationResult validateName(String name) {
        if (isEmpty(name)) {
            return ValidationResult.error("Name is required");
        }

        if (!isValidLength(name, 2, 100)) {
            return ValidationResult.error("Name must be between 2 and 100 characters");
        }

        if (!isLettersWithUnicode(name)) {
            return ValidationResult.error("Name must contain only letters");
        }

        return ValidationResult.success();
    }

    /**
     * Validate email with proper format and length
     * @param email the email to validate
     * @return ValidationResult with success status and error message
     */
    public static ValidationResult validateEmail(String email) {
        if (isEmpty(email)) {
            return ValidationResult.error("Email is required");
        }

        if (!isValidLength(email, 5, 255)) {
            return ValidationResult.error("Email must be between 5 and 255 characters");
        }

        if (!isValidEmail(email)) {
            return ValidationResult.error("Please provide a valid email address");
        }

        return ValidationResult.success();
    }

    /**
     * Validate phone number
     * @param phone the phone to validate
     * @return ValidationResult with success status and error message
     */
    public static ValidationResult validatePhone(String phone) {
        if (isEmpty(phone)) {
            return ValidationResult.error("Phone number is required");
        }

        if (!isValidLength(phone, 9, 20)) {
            return ValidationResult.error("Phone number must be between 9 and 20 characters");
        }

        if (!isValidPhone(phone)) {
            return ValidationResult.error("Please provide a valid phone number");
        }

        return ValidationResult.success();
    }

    /**
     * Validate address
     * @param address the address to validate
     * @return ValidationResult with success status and error message
     */
    public static ValidationResult validateAddress(String address) {
        if (isEmpty(address)) {
            return ValidationResult.error("Address is required");
        }

        if (!isValidLength(address, 5, 500)) {
            return ValidationResult.error("Address must be between 5 and 500 characters");
        }

        return ValidationResult.success();
    }

    /**
     * Result class for validation operations
     */
    public static class ValidationResult {
        private final boolean success;
        private final String errorMessage;

        private ValidationResult(boolean success, String errorMessage) {
            this.success = success;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult success() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean hasError() {
            return !success;
        }
    }
}
