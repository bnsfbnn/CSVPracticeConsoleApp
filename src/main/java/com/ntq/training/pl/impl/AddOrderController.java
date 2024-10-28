package com.ntq.training.pl.impl;

import com.ntq.training.bl.IBaseService;
import com.ntq.training.bl.impl.CustomerService;
import com.ntq.training.bl.impl.OrderService;
import com.ntq.training.bl.impl.ProductService;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.infra.validator.OrderConsistencyChecker;
import com.ntq.training.pl.FunctionController;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class AddOrderController implements FunctionController {
    private final Map<String, IBaseService<?>> services = Map.of(
            "product", new ProductService(),
            "customer", new CustomerService(),
            "order", new OrderService()
    );

    @Override
    public void processFunction(String filePath) {
        log.info("FUNCTION 4.1 - Start loading data from files:  products.origin.csv, customers.origin.csv, orders.origin.csv.");
        Map<Integer, Product> products = loadData("product", filePath, "products" + FileConstants.ORIGIN_CSV_FILE_EXTENSION);
        Map<Integer, Customer> customers = loadData("customer", filePath, "customers" + FileConstants.ORIGIN_CSV_FILE_EXTENSION);
        Map<Integer, Order> orders = loadData("order", filePath, "orders" + FileConstants.ORIGIN_CSV_FILE_EXTENSION);
        orders = OrderConsistencyChecker.checkOrderConsistency(orders, customers, products);
        log.info("FUNCTION 4.1 - Load data from files: products.origin.csv, customers.origin.csv, orders.origin.csv successfully!");
        log.info("FUNCTION 4.1 - Start loading data from orders.new.csv.");
        Map<Integer, Order> newOrders = loadData("order", filePath, "orders" + FileConstants.ORIGIN_CSV_FILE_EXTENSION);
        newOrders = OrderConsistencyChecker.checkOrderConsistency(newOrders, customers, products);
        log.info("FUNCTION 4.1 - Load data from products.new.csv successfully!");
        OrderService orderService = new OrderService();
        orderService.calculateTotalAmountForOrders(orders, products);
        orderService.calculateTotalAmountForOrders(newOrders, products);
        orders = orderService.insert(filePath, orders, newOrders);
        saveData("order", filePath, orders);
    }

    private <T> Map<Integer, T> loadData(String type, String filePath, String fileName) {
        IBaseService<T> service = (IBaseService<T>) services.get(type);
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, fileName).toString();
        return service.loadFile(loaderPath);
    }

    private <T> void saveData(String type, String filePath, Map<Integer, T> data) {
        IBaseService<T> service = (IBaseService<T>) services.get(type);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, type + "s" + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, data);
        log.info("FUNCTION 4.1 - Load data to {}.output.csv successfully!", type);
    }
}
