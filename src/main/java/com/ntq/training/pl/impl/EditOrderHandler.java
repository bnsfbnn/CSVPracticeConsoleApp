package com.ntq.training.pl.impl;

import com.ntq.training.bl.IDataService;
import com.ntq.training.bl.OrderService;
import com.ntq.training.bl.impl.CustomerServiceImpl;
import com.ntq.training.bl.impl.OrderServiceImpl;
import com.ntq.training.bl.impl.ProductServiceImpl;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.infra.validator.OrderConsistencyChecker;
import com.ntq.training.pl.IBaseFunction;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class EditOrderHandler implements IBaseFunction {
    private final Map<String, IDataService<?>> services = Map.of(
            "products", new ProductServiceImpl(),
            "customers", new CustomerServiceImpl(),
            "orders", new OrderServiceImpl()
    );

    @Override
    public void processFunction(String filePath) throws Exception {
        Map<Integer, Product> products = loadData("products", filePath);
        Map<Integer, Customer> customers = loadData("customers", filePath);
        Map<Integer, Order> orders = loadData("orders", filePath);
        orders = OrderConsistencyChecker.checkOrderConsistency(orders, customers, products);

        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "orders" + FileConstants.EDIT_CSV_FILE_EXTENSION).toString();
        Map<Integer, Order> updateOrders = loadData("orders", loaderPath);
        updateOrders = OrderConsistencyChecker.checkOrderConsistency(updateOrders, customers, products);

        OrderService orderService = new OrderServiceImpl();
        orders = orderService.update(filePath, orders, updateOrders);
        orderService.calculateTotalAmountForOrders(orders, products);
        saveData("orders", filePath, orders);
    }

    private <T> Map<Integer, T> loadData(String type, String filePath) throws Exception {
        IDataService<T> service = (IDataService<T>) services.get(type);
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, type + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        return service.loadFile(loaderPath);
    }

    private <T> void saveData(String type, String filePath, Map<Integer, T> data) throws Exception {
        IDataService<T> service = (IDataService<T>) services.get(type);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, type + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, data);
        log.info("FUNCTION 4.2 - Load data to {}.output.csv successfully!", type);
    }
}
