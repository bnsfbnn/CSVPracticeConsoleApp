package com.ntq.training.infra.util;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
public class FileWriterHelper {
    public void writeCsvFile(String filePath, List<String> headers, List<List<String>> records) throws IOException {
        try (
                FileWriter writer = new FileWriter(filePath);
                CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)
        ) {
            csvWriter.writeNext(headers.toArray(new String[0]));
            for (List<String> record : records) {
                csvWriter.writeNext(record.toArray(new String[0]));
            }
        } catch (FileNotFoundException e) {
            log.error("FILE WRITER ERROR: The file at path '{}' was not found.", filePath);
            throw e;
        } catch (IOException e) {
            log.error("FILE WRITER ERROR: Error writing file - {}.", e.getMessage());
            throw e;
        }
    }
}
