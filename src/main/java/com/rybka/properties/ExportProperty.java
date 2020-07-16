package com.rybka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "export")
public class ExportProperty {
    private String folder;
    private String type;
}
