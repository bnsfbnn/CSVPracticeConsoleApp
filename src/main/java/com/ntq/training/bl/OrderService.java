package com.ntq.training.bl;

import com.ntq.training.dal.dto.OrderToAddDTO;
import com.ntq.training.dal.dto.OrderToDeleteDTO;
import com.ntq.training.dal.entity.Order;
import com.ntq.training.dal.entity.Product;

import java.util.Map;

public interface OrderService extends IDataService<Order> {

    Map<Integer, Order> insert(String filePath, Map<Integer, Order> entities, Map<Integer, OrderToAddDTO> newEntities);

    Map<Integer, Order> update(String filePath, Map<Integer, Order> entities, Map<Integer, Order> updateEntities);

    Map<Integer, Order> delete(String filePath, Map<Integer, Order> entities, Map<Integer, OrderToDeleteDTO> deleteEntities);

    Map<Integer, OrderToAddDTO> loadAddingFile(String filePath) throws Exception;

    Map<Integer, OrderToDeleteDTO> loadDeletingFile(String filePath) throws Exception;

    void calculateTotalAmountForOrders(Map<Integer, Order> orders, Map<Integer, Product> products);
}
