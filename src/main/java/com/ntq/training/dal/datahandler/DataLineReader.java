package com.ntq.training.dal.datahandler;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.FileReaderConstants;
import com.ntq.training.infra.validator.ProductValidator;

import java.util.List;
import java.util.function.Function;

public class DataLineReader {

    public static Function<String, Product> mapToProduct = (line) -> {
        String[] fields = line.split(FileReaderConstants.CSV_LINE_SEPARATOR);
        String productId = fields[FileReaderConstants.ProductField.PRODUCT_ID.getIndex()];
        String productName = fields[FileReaderConstants.ProductField.PRODUCT_NAME.getIndex()];
        Double productPrice = Double.parseDouble(fields[FileReaderConstants.ProductField.PRODUCT_PRICE.getIndex()]);
        Integer productStockAvailable = Integer.parseInt(fields[FileReaderConstants.ProductField.PRODUCT_STOCK_AVAILABLE.getIndex()]);
        Product product = new Product(productId, productName, productPrice, productStockAvailable);
        ProductValidator validator = new ProductValidator();
        List<String> errors = validator.validate(product);
        if (!errors.isEmpty()) {
            ErrorLogger.logError("Validation Error", String.join(", ", errors), line, false);
            return null;
        }

    };

    public static Function<String, Customer> mapToCustomer = (line) -> {
        String[] fields = line.split(FileReaderConstants.CSV_LINE_SEPARATOR);
        String customerId = fields[FileReaderConstants.CustomerField.CUSTOMER_ID.getIndex()];
        String customerName = fields[FileReaderConstants.CustomerField.CUSTOMER_NAME.getIndex()];
        String customerEmail = fields[FileReaderConstants.CustomerField.CUSTOMER_EMAIL.getIndex()];
        String customerPhoneNumber = fields[FileReaderConstants.CustomerField.CUSTOMER_PHONE_NUMBER.getIndex()];
        return new Customer(customerId, customerName, customerEmail, customerPhoneNumber);
    };

    public static Function<String, Order> mapToOrder = (line) -> {
        String[] fields = line.split(FileReaderConstants.CSV_LINE_SEPARATOR);
        String orderId = fields[FileReaderConstants.OrderField.ORDER_ID.getIndex()];
        String orderCustomerId = fields[FileReaderConstants.OrderField.ORDER_CUSTOMER_ID.getIndex()];
        String orderProductQuantities = fields[FileReaderConstants.OrderField.ORDER_PRODUCT_QUANTITIES.getIndex()];
        String orderDate = fields[FileReaderConstants.OrderField.ORDER_ORDER_DATE.getIndex()];
        return new Order(orderId, orderCustomerId, orderProductQuantities, orderDate);
    };
}
