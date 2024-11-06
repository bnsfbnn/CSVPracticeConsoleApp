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
import com.ntq.training.pl.CommonDataHandler;
import com.ntq.training.pl.IBaseFunction;
import com.ntq.training.infra.validator.OrderConsistencyChecker;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class LoadDataHandler extends CommonDataHandler implements IBaseFunction {
    @Override
    public void processFunction(String filePath) throws Exception {
        Map<Integer, Product> products = loadData("products", filePath);
        Map<Integer, Customer> customers = loadData("customers", filePath);
        Map<Integer, Order> orders = loadData("orders", filePath);
        orders = OrderConsistencyChecker.checkOrderConsistency(orders, customers, products);

        saveData("products", filePath, products);
        saveData("customers", filePath, customers);
        OrderService orderService = new OrderServiceImpl();
        orderService.calculateTotalAmountForOrders(orders, products);
        saveData("orders", filePath, orders);
    }

}
