package com.ntq.training.bl;

import com.ntq.training.dal.entity.Customer;
import com.ntq.training.dal.entity.Product;

import java.util.Map;

public interface IBaseService<T> {
    Map<Integer, T> loadFile(String filePath);

    void saveFile(String filePath, Map<Integer, T> entities);

    Map<Integer, T> insert(String filePath, Map<Integer, T> entities, Map<Integer, T> newEntities);

    boolean update(String filePath);

    boolean delete(String filePath);
}
