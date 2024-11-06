package com.ntq.training.pl.impl;

import com.ntq.training.bl.OrderService;
import com.ntq.training.bl.impl.OrderServiceImpl;
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
public class EditOrderHandler extends CommonDataHandler implements IBaseFunction {

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
}
