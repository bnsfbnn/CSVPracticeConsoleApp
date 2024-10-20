package com.ntq.training.infra.constants;

public class ValidationMessages {
    public static final String PRODUCT_ID_EMPTY = "Product ID cannot be empty.";
    public static final String PRODUCT_NAME_EMPTY = "Product name cannot be empty.";
    public static final String PRODUCT_PRICE_NEGATIVE = "Product price must be positive.";
    public static final String PRODUCT_STOCK_NEGATIVE = "Product stock must be non-negative.";
    public static final String PRODUCT_ID_DUPLICATE = "Product ID must be unique.";

    public static final String CUSTOMER_ID_EMPTY = "Customer ID cannot be empty.";
    public static final String CUSTOMER_NAME_EMPTY = "Customer name cannot be empty.";
    public static final String CUSTOMER_EMAIL_INVALID = "Invalid customer email.";
    public static final String CUSTOMER_PHONE_INVALID = "Invalid customer phone number.";

    public static final String ORDER_ID_EMPTY = "Order ID cannot be empty.";
    public static final String ORDER_CUSTOMER_ID_EMPTY = "Order customer ID cannot be empty.";
    public static final String ORDER_PRODUCT_QUANTITIES_EMPTY = "Order product quantities cannot be empty.";
    public static final String ORDER_DATE_INVALID = "Invalid order date.";
}
