package com.rybka.config;

import java.util.Map;

public class MapSearchUtil {
    public static <T> Map.Entry<String, T> retrieveMapValue(Map<String, T> map, String searchItem, RuntimeException runtimeException) {
        return map.entrySet().stream()
                .filter(item -> searchItem.equals(item.getKey()))
                .findFirst()
                .orElseThrow(() -> runtimeException);
    }
}
