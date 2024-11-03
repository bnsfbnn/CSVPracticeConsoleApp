package com.ntq.training.bl;

import com.ntq.training.dal.dto.CustomerToDeleteDTO;
import com.ntq.training.dal.entity.Customer;

import java.util.Map;

public interface CustomerService extends IDataService<Customer> {

    Map<Integer, Customer> insert(String filePath, Map<Integer, Customer> entities, Map<Integer, Customer> newEntities);

    Map<Integer, Customer> update(String filePath, Map<Integer, Customer> entities, Map<Integer, Customer> updateEntities);

    Map<Integer, Customer> delete(String filePath, Map<Integer, Customer> entities, Map<Integer, CustomerToDeleteDTO> deleteEntities);

    Map<Integer, CustomerToDeleteDTO> loadDeletingFile(String filePath) throws Exception;
}
