package com.rybka;

import com.rybka.exception.InvalidPropertyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class PropertyReader {
    private final String path;

    public Properties getProperties() {
        Properties properties = new Properties();
        try (FileInputStream in = new FileInputStream(path)) {
            properties.load(in);

            return properties;
        } catch (IOException e) {
            throw new InvalidPropertyException("Unable to get properties from file. Reason: " + e.getMessage());
        }
    }
}
