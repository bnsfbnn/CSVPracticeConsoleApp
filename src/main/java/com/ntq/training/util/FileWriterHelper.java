package com.ntq.training.util;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterHelper {
    public void writeCsvFile(String filePath, List<String> headers, List<List<String>> records) {
        try (
                FileWriter writer = new FileWriter(filePath);
                CSVWriter csvWriter = new CSVWriter(writer)
        ) {
            csvWriter.writeNext(headers.toArray(new String[0]));
            for (List<String> record : records) {
                csvWriter.writeNext(record.toArray(new String[0]));
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void writeTxtFile(String filePath, String lines) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(lines);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
