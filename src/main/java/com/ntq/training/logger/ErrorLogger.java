package com.ntq.training.logger;

import com.ntq.training.util.FileWriterHelper;

public class ErrorLogger {
    private static final String ERROR_LOG_FILE = "error.output.txt";
    private static final FileWriterHelper fileWriterHelper = new FileWriterHelper();

    public static void logException(String message, Exception exception) {
        String logEntry = getCurrentTimestamp() + " - Exception: " + message + " - " + exception.getMessage();
        fileWriterHelper.writeTxtFile(ERROR_LOG_FILE, logEntry);
    }

    public static void logValidationError(String message, String line) {
        String logEntry = getCurrentTimestamp() + " - Validation Error: " + message + " - Line: " + line;
        fileWriterHelper.writeTxtFile(ERROR_LOG_FILE, logEntry);
    }

    private static String getCurrentTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}
