package com.ntq.training.dal.datahandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DataLoader<T> {
    public List<T> loadData(String filePath, Function<String, T> mapToEntity) {
        List<T> entities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                T entity = mapToEntity.apply(line);
                entities.add(entity);
            }
        } catch (IOException e) {
            System.out.println("DATA: " + e.getMessage());
        }
        return entities;
    }
}
