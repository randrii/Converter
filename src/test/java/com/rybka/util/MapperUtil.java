package com.rybka.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class MapperUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> String mapToJson(T data) {
        return mapper.writeValueAsString(data);
    }
}
