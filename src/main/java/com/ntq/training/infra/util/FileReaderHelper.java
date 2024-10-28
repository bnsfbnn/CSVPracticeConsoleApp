package com.ntq.training.infra.util;

import com.ntq.training.infra.validator.FileValidator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Slf4j
public class FileReaderHelper {

    public Map<Integer, List<String>> readCsvFile(String filePath) {
        Map<Integer, List<String>> records = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] values;
            String[] header = csvReader.readNext();
            if (!FileValidator.isValidHeader(header)) {
                return Collections.emptyMap();
            }
            int rowIndex = 1;
            while ((values = csvReader.readNext()) != null) {
                if (!FileValidator.isValidRow(rowIndex, header, values)) {
                    ++rowIndex;
                    continue;
                }
                List<String> row = new ArrayList<>(Arrays.asList(values));
                records.put(rowIndex++, row);
            }

        } catch (FileNotFoundException e) {
            log.error("FILE READER ERROR: The file at path '{}' was not found.", filePath);
            System.exit(0);
        } catch (IOException | CsvException e) {
            log.error("FILE READER ERROR: Error reading file - {}.", e.getMessage());
            System.exit(0);
        }
        return records;
    }
}
