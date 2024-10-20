package com.ntq.training.infra.constants;

import lombok.Getter;

public class FileReaderConstants {
    public static final String CSV_LINE_SEPARATOR = ",";

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