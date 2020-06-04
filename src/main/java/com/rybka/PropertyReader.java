package com.rybka;

import com.rybka.exception.InvalidPropertyException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
    private final String path;

    public PropertyReader(String path) {
        this.path = path;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(path)) {
            properties.load(in);

            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidPropertyException("Unable to get properties from file. Reason: " + e.getMessage());
        }
    }
}
