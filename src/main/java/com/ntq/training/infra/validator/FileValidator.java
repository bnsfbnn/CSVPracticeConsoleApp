package com.ntq.training.infra.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class FileValidator {

    public static boolean isValidHeader(String[] header) {
        return !Objects.equals(header[0], "");
    }

    public static boolean isValidRow(String fileName, int rowIndex, String[] header, String[] values) {
        if (values.length != header.length) {
            log.error("FILE VALIDATION ERROR: Row {} in file {} must have {} columns, but found {}.", rowIndex, fileName, header.length, values.length);
            return false;
        }
        List<String> missingColumns = new ArrayList<>();
        for (int i = 0; i < header.length; i++) {
            if (values[i] == null || values[i].trim().isEmpty()) {
                missingColumns.add(header[i]);
            }
        }
        if (!missingColumns.isEmpty()) {
            log.error("FILE VALIDATION ERROR: Row {} in file {} is missing values for columns: {}", rowIndex, fileName, String.join(", ", missingColumns));
            return false;
        }
        return true;
    }
}
