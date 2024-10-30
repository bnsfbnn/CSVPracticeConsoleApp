package com.ntq.training.pl.impl;

import com.ntq.training.bl.IDataService;
import com.ntq.training.bl.OrderService;
import com.ntq.training.bl.impl.CustomerServiceImpl;
import com.ntq.training.bl.impl.OrderServiceImpl;
import com.ntq.training.bl.impl.ProductServiceImpl;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.pl.IBaseController;
import com.ntq.training.infra.validator.OrderConsistencyChecker;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class LoadDataController implements IBaseController {
    private final Map<String, Object> services = Map.of(
            "products", new ProductServiceImpl(),
            "customers", new CustomerServiceImpl(),
            "orders", new OrderServiceImpl()
    );

    @Override
    public void processFunction(String filePath) {
        log.info("FUNCTION 1 - Start loading data from files:  products.origin.csv, customers.origin.csv, orders.origin.csv.");
        Map<Integer, Product> products = loadData("products", filePath);
        Map<Integer, Customer> customers = loadData("customers", filePath);
        Map<Integer, Order> orders = loadData("orders", filePath);
        orders = OrderConsistencyChecker.checkOrderConsistency(orders, customers, products);
        log.info("FUNCTION 1 - Load data from files: products.origin.csv, customers.origin.csv, orders.origin.csv successfully!");
        saveData("products", filePath, products);
        saveData("customers", filePath, customers);
        OrderService orderService = new OrderServiceImpl();
        orderService.calculateTotalAmountForOrders(orders, products);
        saveData("orders", filePath, orders);
    }

    private <T> Map<Integer, T> loadData(String type, String filePath) {
        IDataService<T> service = (IDataService<T>) services.get(type);
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, type + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        return service.loadFile(loaderPath);
    }

    private <T> void saveData(String type, String filePath, Map<Integer, T> data) {
        IDataService<T> service = (IDataService<T>) services.get(type);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, type + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, data);
        log.info("FUNCTION 1 - Load data to {}.output.csv successfully!", type);
    }
}
