package com.ntq.training.infra.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileValidator {

    public static boolean isValidHeader(String[] header) {
        if (header == null || header.length == 0) {
            log.error("FILE VALIDATION ERROR: The file is empty or has no header.");
            return false;
        }
        return true;
    }

    public static boolean isValidRow(int rowIndex, String[] header, String[] values) {
        if (values.length != header.length) {
            log.error("FILE VALIDATION ERROR: Row {} must have {} columns, but found {}", rowIndex, header.length, values.length);
            return false;
        }
        List<String> missingColumns = new ArrayList<>();
        for (int i = 0; i < header.length; i++) {
            if (values[i] == null || values[i].trim().isEmpty()) {
                missingColumns.add(header[i]);
            }
        }
        if (!missingColumns.isEmpty()) {
            log.error("FILE VALIDATION ERROR: Row {} is missing values for columns: {}", rowIndex, missingColumns);
            return false;
        }
        return true;
    }
}
