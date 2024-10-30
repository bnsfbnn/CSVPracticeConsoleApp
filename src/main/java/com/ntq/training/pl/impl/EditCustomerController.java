package com.ntq.training.pl.impl;

import com.ntq.training.bl.CustomerService;
import com.ntq.training.bl.impl.CustomerServiceImpl;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.pl.IBaseController;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class EditCustomerController implements IBaseController {
    @Override
    public void processFunction(String filePath) {
        CustomerService service = new CustomerServiceImpl();
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> customers = service.loadFile(loaderPath);
        log.info("FUNCTION 3.3 - Load data from customers.origin.csv successfully!");

        String updateLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.EDIT_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> updateCustomers = service.loadFile(updateLoaderPath);
        log.info("FUNCTION 3.3 - Load data from customers.edit.csv successfully!");

        customers = service.update(filePath, customers, updateCustomers);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, customers);
        log.info("FUNCTION 3.3 - Load data to customers.output.csv successfully!");
    }
}
