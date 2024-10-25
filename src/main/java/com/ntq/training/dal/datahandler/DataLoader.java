package com.ntq.training.dal.datahandler;

import com.ntq.training.util.FileReaderHelper;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class DataLoader<T> {
    private final FileReaderHelper fileReaderHelper = new FileReaderHelper();

    public Map<Integer, T> loadData(String filePath, BiFunction<Integer, List<String>, Optional<T>> mapToEntity) {
        return fileReaderHelper.readCsvFile(filePath).entrySet().stream()
                .map(entry -> {
                    Integer rowIndex = entry.getKey();
                    List<String> line = entry.getValue();
                    Optional<T> entity = mapToEntity.apply(rowIndex, line);
                    return entity.map(e -> Map.entry(rowIndex, e)).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
    }
}
