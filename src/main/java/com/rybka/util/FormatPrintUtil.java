package com.rybka.util;

import com.rybka.model.CurrencyHistory;

import java.util.List;

public class FormatPrintUtil {

    private static final String formatter = "%s -> %s\t%.2f\t%.2f\t%.2f\t%.16s";

    public static void printHistory(List<CurrencyHistory> historyList) {
        historyList.stream()
                .map(history ->
                        String.format(formatter,
                                history.getBase(), history.getTarget(), history.getAmount(), history.getRate(), history.getTotal(), history.getDate()))
                .forEach(System.out::println);
    }
}
