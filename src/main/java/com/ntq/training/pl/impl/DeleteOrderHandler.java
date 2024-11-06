package com.ntq.training.pl.impl;

import com.ntq.training.bl.OrderService;
import com.ntq.training.bl.impl.OrderServiceImpl;
import com.ntq.training.dal.dto.OrderToDeleteDTO;
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
public class DeleteOrderHandler extends CommonDataHandler implements IBaseFunction {
    OrderService orderService = new OrderServiceImpl();

    @Override
    public void processFunction(String filePath) throws Exception {
        Map<Integer, Product> products = loadData("products", filePath);
        Map<Integer, Customer> customers = loadData("customers", filePath);
        Map<Integer, Order> orders = loadData("orders", filePath);
        orders = OrderConsistencyChecker.checkOrderConsistency(orders, customers, products);

        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "orders" + FileConstants.DELETE_CSV_FILE_EXTENSION).toString();
        Map<Integer, OrderToDeleteDTO> deleleOrders = orderService.loadDeletingFile(loaderPath);

        orders = orderService.delete(filePath, orders, deleleOrders);
        orderService.calculateTotalAmountForOrders(orders, products);
        saveData("orders", filePath, orders);
    }
}
