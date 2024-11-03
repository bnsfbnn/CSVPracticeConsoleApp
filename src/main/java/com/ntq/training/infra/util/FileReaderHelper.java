package com.ntq.training.infra.util;

import com.ntq.training.infra.exception.FileValidationException;
import com.ntq.training.infra.validator.FileValidator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class FileReaderHelper {

    public Map<Integer, List<String>> readCsvFile(String filePath, Boolean isValidRowCheck) throws IOException, CsvException, FileValidationException {
        Map<Integer, List<String>> records = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] values;
            String[] header = csvReader.readNext();
            if (!FileValidator.isValidHeader(header)) {
                log.error("FILE VALIDATION ERROR: The file is empty or has no header.");
                int v = 0;
                throw new FileValidationException();
            }
            int rowIndex = 2;
            Path path = Paths.get(filePath);
            while ((values = csvReader.readNext()) != null) {
                if (isValidRowCheck) {
                    if (!FileValidator.isValidRow(path.getFileName().toString(), rowIndex, header, values)) {
                        ++rowIndex;
                        continue;
                    }
                }
                List<String> row = new ArrayList<>(Arrays.asList(values));
                records.put(rowIndex++, row);
            }
        } catch (FileNotFoundException e) {
            log.error("FILE READER ERROR: The file at path '{}' was not found.", filePath);
            throw e;
        } catch (IOException | CsvException e) {
            log.error("FILE READER ERROR: Error reading file.");
            throw e;
        }
        return records;
    }
}
