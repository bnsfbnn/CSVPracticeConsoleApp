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
public class EditCustomerHandler implements IBaseFunction {
    @Override
    public void processFunction(String filePath) {
        CustomerService service = new CustomerServiceImpl();
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> customers = service.loadFile(loaderPath);

        String updateLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.EDIT_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> updateCustomers = service.loadFile(updateLoaderPath);

        customers = service.update(filePath, customers, updateCustomers);
        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, "products" + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, customers);
    }
}