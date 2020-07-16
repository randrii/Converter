package com.rybka.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeResponse {
    @JsonProperty("result")
    private String success;

    @JsonProperty("time_last_update")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeLastUpdate;

    @JsonProperty("time_next_update")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeNextUpdate;

    @JsonProperty("base")
    private String base;

    @JsonAlias({"conversion_rates", "rates"})
    private Map<String, Double> rates;
}
