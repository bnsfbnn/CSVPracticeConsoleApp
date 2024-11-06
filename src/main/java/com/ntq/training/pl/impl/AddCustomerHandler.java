package com.ntq.training.pl.impl;

import com.ntq.training.bl.CustomerService;
import com.ntq.training.bl.impl.CustomerServiceImpl;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.pl.IBaseFunction;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class AddCustomerHandler implements IBaseFunction {
    CustomerService service = new CustomerServiceImpl();
    
    @Override
    public void processFunction(String filePath) throws Exception {
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> customers = service.loadFile(loaderPath);

        String newLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.NEW_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> newCustomers = service.loadFile(newLoaderPath);

        customers = service.insert(filePath, customers, newCustomers);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, customers);
    }
}
