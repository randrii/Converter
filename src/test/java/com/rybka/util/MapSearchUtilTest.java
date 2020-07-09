package com.rybka.util;

import com.rybka.constant.Messages;
import com.rybka.exception.InvalidPropertyException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapSearchUtilTest {

    @Test
    public void testOnCorrectChoice() {

        // given
        var exportType = "csv";
        var exportConfigMap = Map.of(
                "console", "console",
                "csv", "csv",
                "json", "json");

        // when
        var actualResult = MapSearchUtil.retrieveMapValue(exportConfigMap, exportType, new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG));

        // then
        assertEquals(exportType, actualResult);
    }

    @Test
    public void testOnException() {

        // given
        var testExportType = "xml";
        var exportConfigMap = Map.of(
                "console", "console",
                "csv", "csv",
                "json", "json");

        // then
        assertThrows(InvalidPropertyException.class, () -> MapSearchUtil.retrieveMapValue(exportConfigMap, testExportType, new InvalidPropertyException(Messages.PROPERTY_EXCEPTION_MSG)));
    }
}