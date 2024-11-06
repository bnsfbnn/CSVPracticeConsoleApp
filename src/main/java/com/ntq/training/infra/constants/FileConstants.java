package com.ntq.training.infra.constants;

import lombok.Getter;

public class FileConstants {
    public static final String PRODUCT_QUANTITIES_SEPARATOR = ";";
    public static final String PRODUCT_QUANTITY_SEPARATOR = ":";
    public static final String INPUT_CSV_SUB_FOLDER_PATH = "input-files";
    public static final String OUTPUT_CSV_SUB_FOLDER_PATH = "output-files";
    public static final String ORIGIN_CSV_FILE_EXTENSION = ".origin.csv";
    public static final String OUTPUT_CSV_FILE_EXTENSION = ".output.csv";
    public static final String NEW_CSV_FILE_EXTENSION = ".new.csv";
    public static final String EDIT_CSV_FILE_EXTENSION = ".edit.csv";
    public static final String DELETE_CSV_FILE_EXTENSION = ".delete.csv";
    public static final String SEARCH_CSV_FILE_EXTENSION = ".search.csv";


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

    @Getter
    public enum ProductOnlyIdField {
        PRODUCT_ID(0);

        private final int index;

        ProductOnlyIdField(int index) {
            this.index = index;
        }
    }

    @Getter
    public enum CustomerToDeleteField {
        CUSTOMER_PHONE_NUMBER(0);

        private final int index;

        CustomerToDeleteField(int index) {
            this.index = index;
        }
    }

    @Getter
    public enum OrderToAddField {
        ORDER_CUSTOMER_ID(0),
        ORDER_PRODUCT_QUANTITIES(1),
        ORDER_ORDER_DATE(2);

        private final int index;

        OrderToAddField(int index) {
            this.index = index;
        }
    }

    @Getter
    public enum OrderToDeleteField {
        ORDER_ID(0);

        private final int index;

        OrderToDeleteField(int index) {
            this.index = index;
        }
    }
}