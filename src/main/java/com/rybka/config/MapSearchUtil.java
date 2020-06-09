package com.rybka.config;

import com.rybka.exception.InvalidPropertyException;

import java.util.Map;
import java.util.Optional;

public class MapSearchUtil {
    public static <T> T retrieveMapValue(Map<String, T> map, String searchItem) {
        return Optional.ofNullable(map.get(searchItem))
                .orElseThrow(() -> new InvalidPropertyException("Unsupported export type or exchange source."));
    }
}
