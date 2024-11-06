package com.ntq.training.pl.impl;

import com.ntq.training.bl.OrderService;
import com.ntq.training.bl.ProductService;
import com.ntq.training.bl.impl.OrderServiceImpl;
import com.ntq.training.bl.impl.ProductServiceImpl;
import com.ntq.training.dal.dto.ProductOnlyIdDTO;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.infra.validator.OrderConsistencyChecker;
import com.ntq.training.pl.CommonDataHandler;
import com.ntq.training.pl.IBaseFunction;

import java.nio.file.Paths;
import java.util.Map;

public class SearchOrdersByProductHandler extends CommonDataHandler implements IBaseFunction {
    ProductService productService = new ProductServiceImpl();
    OrderService orderService = new OrderServiceImpl();

    @Override
    public void processFunction(String filePath) throws Exception {
        Map<Integer, Product> products = loadData("products", filePath);
        Map<Integer, Customer> customers = loadData("customers", filePath);
        Map<Integer, Order> orders = loadData("orders", filePath);
        orders = OrderConsistencyChecker.checkOrderConsistency(orders, customers, products);

        String searchLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.SEARCH_CSV_FILE_EXTENSION).toString();
        Map<Integer, ProductOnlyIdDTO> searchProducts = productService.loadOnlyIdFieldFile(searchLoaderPath);

        Map<Integer, Order> ordersSearchByProductId = orderService.searchOrderByProductId(orders, searchProducts);
        saveData("orders", filePath, ordersSearchByProductId);
    }
}
