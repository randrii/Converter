package com.rybka.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExportType {
    CONSOLE("console"),
    CSV("csv"),
    JSON("json");

    private final String type;
}
