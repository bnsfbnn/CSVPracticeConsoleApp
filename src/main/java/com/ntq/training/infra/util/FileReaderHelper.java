package com.ntq.training.infra.util;

import com.ntq.training.infra.validator.FileValidator;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
public class FileReaderHelper {

    public Map<Integer, List<String>> readCsvFile(String filePath, Boolean isValidRowCheck) throws IOException, CsvException {
        Map<Integer, List<String>> records = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String[] values;
            String[] header = csvReader.readNext();
            int rowIndex = 1;
            Path path = Paths.get(filePath);
            while ((values = csvReader.readNext()) != null) {
                values = Arrays.stream(values)
                        .map(String::trim)
                        .toArray(String[]::new);
                if (values.length == 0 || Arrays.stream(values).allMatch(String::isEmpty)) {
                    continue;
                }
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
