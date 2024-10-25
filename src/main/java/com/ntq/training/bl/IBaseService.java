package com.ntq.training.bl;

import java.util.Map;

public interface IBaseService<T> {
    Map<Integer, T> loadFile(String filePath);

    void saveFile(String filePath, Map<Integer, T> entities);

    boolean insert(String filePath);

    boolean update(String filePath);

    boolean delete(String filePath);
}
