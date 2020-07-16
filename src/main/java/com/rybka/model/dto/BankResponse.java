package com.rybka.model.dto;

import com.rybka.model.BankData;
import com.rybka.model.StatisticalRate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BankResponse {
    private List<BankData> bankDataList;
    private StatisticalRate statisticalRate;
}
