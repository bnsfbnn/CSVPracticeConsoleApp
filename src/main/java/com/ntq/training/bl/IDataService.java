package com.ntq.training.bl;

import java.util.Map;

public interface IDataService<T> {
    Map<Integer, T> loadFile(String filePath) throws Exception;

    void saveFile(String filePath, Map<Integer, T> entities) throws Exception;
}
