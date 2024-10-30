package com.ntq.training.pl.impl;

import com.ntq.training.bl.CustomerService;
import com.ntq.training.bl.impl.CustomerServiceImpl;
import com.ntq.training.dal.dto.CustomerToDeleteDTO;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.infra.constants.FileConstants;
import com.ntq.training.pl.IBaseController;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class DeleteCustomerController implements IBaseController {
    @Override
    public void processFunction(String filePath) {
        CustomerService service = new CustomerServiceImpl();
        String loaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.ORIGIN_CSV_FILE_EXTENSION).toString();
        Map<Integer, Customer> customers = service.loadFile(loaderPath);
        log.info("FUNCTION 3.1 - Load data from customers.origin.csv successfully!");

        String deleteLoaderPath = Paths.get(filePath, FileConstants.INPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.DELETE_CSV_FILE_EXTENSION).toString();
        Map<Integer, CustomerToDeleteDTO> deleteCustomers = service.loadDeletingFile(deleteLoaderPath);
        customers = service.delete(filePath, customers, deleteCustomers);
        log.info("FUNCTION 3.1 - Load data from customers.delete.csv successfully!");

        String writerPath = Paths.get(filePath, FileConstants.OUTPUT_CSV_SUB_FOLDER_PATH, "customers" + FileConstants.OUTPUT_CSV_FILE_EXTENSION).toString();
        service.saveFile(writerPath, customers);
        log.info("FUNCTION 3.1 - Load data to customers.output.csv successfully!");
    }
}
