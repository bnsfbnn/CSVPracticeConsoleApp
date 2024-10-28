package com.ntq.training.pl.impl;

import com.ntq.training.bl.IBaseService;
import com.ntq.training.bl.impl.CustomerService;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.pl.FunctionController;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class AddCustomerController implements FunctionController {
    @Override
    public void processFunction(String filePath) {
        IBaseService<Customer> service = new CustomerService();
        log.info("FUNCTION 3.2 - Start loading data from customers.origin.csv.");
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> customers = service.loadFile(loaderPath);
        log.info("FUNCTION 3.2 - Load data from customers.origin.csv successfully!");
        log.info("FUNCTION 3.2 - Start loading data from customers.new.csv.");
        String newLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.NEW_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> newCustomers = service.loadFile(newLoaderPath);
        log.info("FUNCTION 3.2 - Load data from customers.new.csv successfully!");
        customers = service.insert(filePath, customers, newCustomers);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, customers);
        log.info("FUNCTION 3.2 - Load data to customers.output.csv successfully!");
    }
}
