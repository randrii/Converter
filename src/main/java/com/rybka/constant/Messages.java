package com.rybka.constant;

public final class Messages {
    public static final String DB_CONNECTION_EXCEPTION_MSG = "Connection isn't set.";
    public static final String USER_DATA_EXCEPTION_MSG = "Entered data are invalid.";
    public static final String PROPERTY_EXCEPTION_MSG = "Unsupported export type or exchange source.";
    public static final String API_CALL_EXCEPTION_MSG = "An error appears while calling %s API. Reason: ";
    public static final String BASE_CURRENCY_EXCEPTION_MSG = "Target currency not found!";
    public static final String EXPORT_EXCEPTION_MSG = "Unable to export to %s file. Reason: ";
    public static final String COMMAND_EXCEPTION_MSG = "Incorrect command to implement.";

    public static final String LOG_COMMAND_ERROR_MSG = "Unsupported or invalid input. Reason: ";
    public static final String LOG_EXPORT_INFO_MSG = "Exporting history to %s file.";
    public static final String LOG_HISTORY_ERROR_MSG = "Some issues have been occurred while returning history. Reason: ";
    public static final String LOG_INVALID_VALUE_ERROR_MSG = "Please enter valid value for exchange.";
    public static final String LOG_CONNECTION_ERROR_MSG = "Unable to connect to DB while performing action. Reason: ";
    public static final String LOG_CONVERSION_ERROR_MSG = "Some issues have been occurred during conversion. Reason: ";

    public static final String INPUT_VALUE_MSG = "Enter value: ";
    public static final String INPUT_BASE_MSG = "Insert base currency: ";
    public static final String INPUT_TARGET_MSG = "Insert target currency: ";
    public static final String INPUT_OPTION_MSG = "Type option here: ";

    public static final String EXCHANGE_FORMAT = "%.4f %s -> %.4f %s";

    public static final String LOG_BANK_DATA_EXCEPTION_MSG = "Cannot create bank data object. Reason: ";
}
