package com.ntq.training.constants;

import lombok.Getter;

@Getter
public enum ValidationUniqueFields {
    PRODUCT_IDS("ProductIds"),

    CUSTOMER_IDS("CustomerIds"),

    CUSTOMER_EMAILS("CustomerEmails"),

    CUSTOMER_PHONE_NUMBERS("CustomerPhoneNumbers"),

    ORDER_IDS("OrderIds");

    private final String fieldName;

    ValidationUniqueFields(String fieldName) {
        this.fieldName = fieldName;
    }
}
