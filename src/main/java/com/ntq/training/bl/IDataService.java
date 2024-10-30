package com.ntq.training.bl;

import java.util.Map;

public interface IDataService<T> {
    Map<Integer, T> loadFile(String filePath);

    void saveFile(String filePath, Map<Integer, T> entities);
}
