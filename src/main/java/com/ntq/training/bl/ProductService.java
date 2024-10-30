package com.ntq.training.bl;

import com.ntq.training.dal.dto.ProductToDeleteDTO;
import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Product;

import java.util.Map;

public interface ProductService extends IDataService<Product> {

    Map<Integer, Product> insert(String filePath, Map<Integer, Product> entities, Map<Integer, Product> newEntities);

    Map<Integer, Product> update(String filePath, Map<Integer, Product> entities, Map<Integer, Product> updateEntities);

    Map<Integer, Product> delete(String filePath, Map<Integer, Product> entities, Map<Integer, ProductToDeleteDTO> deleteEntities);

    Map<Integer, ProductToDeleteDTO> loadDeletingFile(String filePath);
}
