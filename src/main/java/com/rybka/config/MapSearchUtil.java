package com.rybka.config;

import java.util.Map;
import java.util.Optional;

public class MapSearchUtil {
    public static <T> T retrieveMapValue(Map<String, T> map, String searchItem, RuntimeException exception) {
        return Optional.ofNullable(map.get(searchItem))
                .orElseThrow(() -> exception);
    }
}
