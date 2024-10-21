package com.ntq.training.dal.datahandler;

import com.ntq.training.constants.FileReaderConstants;
import com.ntq.training.logger.ErrorLogger;
import com.ntq.training.util.FileReaderHelper;
import com.ntq.training.validator.Validator;
import com.ntq.training.validator.ValidatorFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataLoader<T> {
    private final FileReaderHelper fileReaderHelper = new FileReaderHelper();

    public List<T> loadData(String filePath, Function<List<String>, T> mapToEntity, Class<T> clazz) {
        Optional<Validator<T>> validator = ValidatorFactory.getValidator(clazz);
        return fileReaderHelper.readCsvFile(filePath).stream()
                .map(line -> {
                    T entity = mapToEntity.apply(line);
                    Map<String, Set<String>> existFields = new TreeMap<>();
                    if (entity != null && validator.isPresent()) {
                        List<String> errors = validator.get().validate(entity, existFields);
                        if (!errors.isEmpty()) {
                            ErrorLogger.logValidationError("Lỗi", String.join(FileReaderConstants.ERROR_SEPARATOR, errors));
                            return null;
                        }
                    }
                    return entity;
                })
                .collect(Collectors.toList());
    }
}
