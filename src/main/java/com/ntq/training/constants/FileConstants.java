package com.ntq.training.constants;

import lombok.Getter;

public class FileConstants {
    public static final String PRODUCT_QUANTITIES_SEPARATOR = ";";
    public static final String PRODUCT_QUANTITY_SEPARATOR = ":";
    public static final String INPUT_CSV_SUB_FOLDER_PATH = "input-files";
    public static final String OUTPUT_CSV_SUB_FOLDER_PATH = "output-files";
    public static final String PRODUCT_ORIGIN_CSV_FILE_NAME = "products.origin.csv";
    public static final String PRODUCT_OUTPUT_CSV_FILE_NAME = "products.output.csv";
    public static final String CUSTOMER_ORIGIN_CSV_FILE_NAME = "customers.origin.csv";
    public static final String CUSTOMER_OUTPUT_CSV_FILE_NAME = "customers.output.csv";
    public static final String ORDER_ORIGIN_CSV_FILE_NAME = "orders.origin.csv";
    public static final String ORDER_OUTPUT_CSV_FILE_NAME = "orders.output.csv";

    @Getter
    public enum ProductField {
        PRODUCT_ID(0),
        PRODUCT_NAME(1),
        PRODUCT_PRICE(2),
        PRODUCT_STOCK_AVAILABLE(3);

        private final int index;

        ProductField(int index) {
            this.index = index;
        }

    }

    @Getter
    public enum CustomerField {
        CUSTOMER_ID(0),
        CUSTOMER_NAME(1),
        CUSTOMER_EMAIL(2),
        CUSTOMER_PHONE_NUMBER(3);

        private final int index;

        CustomerField(int index) {
            this.index = index;
        }
    }

    @Getter
    public enum OrderField {
        ORDER_ID(0),
        ORDER_CUSTOMER_ID(1),
        ORDER_PRODUCT_QUANTITIES(2),
        ORDER_ORDER_DATE(3);

        private final int index;

        OrderField(int index) {
            this.index = index;
        }

    }
}