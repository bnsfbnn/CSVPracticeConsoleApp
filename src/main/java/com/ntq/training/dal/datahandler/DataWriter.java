package com.ntq.training.dal.datahandler;

import com.ntq.training.util.FileWriterHelper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DataWriter<T> {
    public void saveData(Map<Integer, T> entities, String filePath,
                         Function<T, List<String>> headersFunction,
                         Function<T, List<String>> rowMapperFunction) {
        FileWriterHelper fileWriterHelper = new FileWriterHelper();

        List<String> headers = headersFunction.apply(null);

        List<List<String>> records = entities.entrySet().stream()
                .map(entry -> rowMapperFunction.apply(entry.getValue()))
                .toList();

        fileWriterHelper.writeCsvFile(filePath, headers, records);
    }
}
