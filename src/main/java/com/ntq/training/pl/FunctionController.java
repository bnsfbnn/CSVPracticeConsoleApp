package com.ntq.training.pl;

import com.ntq.training.bl.IBaseService;
import com.ntq.training.bl.impl.CustomerServiceImpl;
import com.ntq.training.bl.impl.ProductServiceImpl;
import com.ntq.training.constants.FileConstants;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Product;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class FunctionController {

    public void run(String functionCode, String filePath) {
        switch (functionCode) {
            case "1":
                log.info("FUNCTION 1 - Waiting for loading data from products.origin.csv !!!");
                IBaseService<Product> productService = new ProductServiceImpl();
                String productLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, FileConstants.PRODUCT_ORIGIN_CSV_FILE_NAME).toString();
                String productWriterPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, FileConstants.PRODUCT_OUTPUT_CSV_FILE_NAME).toString();
                Map<Integer, Product> products = productService.loadFile(productLoaderPath);
                productService.saveFile(productWriterPath, products);
                log.info("FUNCTION 1 - Load data to products.output.csv successfully completed !!!");
                log.info("FUNCTION 1 - Waiting for loading data from customers.origin.csv !!!");
                IBaseService<Customer> customerService = new CustomerServiceImpl();
                String customerLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, FileConstants.CUSTOMER_ORIGIN_CSV_FILE_NAME).toString();
                String customerWriterPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, FileConstants.CUSTOMER_OUTPUT_CSV_FILE_NAME).toString();
                Map<Integer, Customer> customers = customerService.loadFile(customerLoaderPath);
                customerService.saveFile(customerWriterPath, customers);
                log.info("FUNCTION 1 - Load data to customers.output.csv successfully completed !!!");
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                break;
            default:
                System.out.println("Invalid function code.");
        }
    }
}
