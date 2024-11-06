package com.ntq.training.pl.impl;

import com.ntq.training.bl.ProductService;
import com.ntq.training.bl.impl.ProductServiceImpl;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.validator.OrderConsistencyChecker;
import com.ntq.training.pl.CommonDataHandler;
import com.ntq.training.pl.IBaseFunction;

import java.util.Map;

public class SearchTopProductHandler extends CommonDataHandler implements IBaseFunction {
    ProductService productService = new ProductServiceImpl();

    @Override
    public void processFunction(String filePath) throws Exception {
        Map<Integer, Product> products = loadData("products", filePath);
        Map<Integer, Customer> customers = loadData("customers", filePath);
        Map<Integer, Order> orders = loadData("orders", filePath);
        orders = OrderConsistencyChecker.checkOrderConsistency(orders, customers, products);

        Map<Integer, Product> top3MostProduct = productService.findTop3ProductHasMostOrder(products, orders);
        saveData("products", filePath, top3MostProduct);
    }
}
