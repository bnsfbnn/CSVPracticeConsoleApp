package com.ntq.training.bl;

import com.ntq.training.dal.dto.ProductOnlyIdDTO;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;

import java.util.Map;

public interface ProductService extends IDataService<Product> {

    Map<Integer, Product> insert(String filePath, Map<Integer, Product> entities, Map<Integer, Product> newEntities);

    Map<Integer, Product> update(String filePath, Map<Integer, Product> entities, Map<Integer, Product> updateEntities);

    Map<Integer, Product> delete(String filePath, Map<Integer, Product> entities, Map<Integer, ProductOnlyIdDTO> deleteEntities);

    Map<Integer, ProductOnlyIdDTO> loadOnlyIdFieldFile(String filePath) throws Exception;

    Map<Integer, Product> findTop3ProductHasMostOrder(Map<Integer, Product> products, Map<Integer, Order> orders);
}
