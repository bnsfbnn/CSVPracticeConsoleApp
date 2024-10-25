package com.ntq.training.validator;

import java.math.BigDecimal;

public class CommonParserValidator {
    public static boolean isPositive(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isPositive(Integer value) {
        return value != null && value >= 0;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.matches("^\\d{10}$");
    }

}
