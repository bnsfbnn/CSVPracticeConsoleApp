package com.ntq.training.infra.validator;

public class CommonValidator {
    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    public static boolean isPositive(Double value) {
        return value != null && value > 0;
    }
    public static boolean isNonNegative(Integer value) {
        return value != null && value >= 0;
    }
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\d{10}$");
    }
    public static boolean isValidDate(String date) {
        return date != null && date.matches("^\\d{4}-\\d{2}-\\d{2}$");
    }
    public static boolean isWithinRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
}
