package com.ntq.training.pl.impl;

import com.ntq.training.bl.OrderService;
import com.ntq.training.bl.impl.OrderServiceImpl;
import com.ntq.training.dal.dto.OrderToAddDTO;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.infra.validator.OrderConsistencyChecker;
import com.ntq.training.pl.CommonDataHandler;
import com.ntq.training.pl.IBaseFunction;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class AddOrderHandler extends CommonDataHandler implements IBaseFunction {
    OrderService orderService = new OrderServiceImpl();

    @Override
    public void processFunction(String filePath) throws Exception {
        Map<Integer, Product> products = loadData("products", filePath);
        Map<Integer, Customer> customers = loadData("customers", filePath);
        Map<Integer, Order> orders = loadData("orders", filePath);
        orders = OrderConsistencyChecker.checkOrderConsistency(orders, customers, products);
        log.info("FUNCTION 4.1 - Load data from files: products.origin.csv, customers.origin.csv, orders.origin.csv successfully!");

        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "orders" + FileConstants.NEW_CSV_FILE_EXTENSION).toString();
        Map<Integer, OrderToAddDTO> newOrders = orderService.loadAddingFile(loaderPath);
        newOrders = OrderConsistencyChecker.checkOrderToAddDTOConsistency(newOrders, customers, products);
        log.info("FUNCTION 4.1 - Load data from products.new.csv successfully!");

        orders = orderService.insert(filePath, orders, newOrders);
        orderService.calculateTotalAmountForOrders(orders, products);
        saveData("orders", filePath, orders);
    }
}
