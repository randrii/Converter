package com.rybka.service.exchange;

import com.rybka.constant.BankAggregatorConstants;
import com.rybka.constant.Messages;
import com.rybka.exception.InvalidResponseException;
import com.rybka.exception.ResourceProcessingException;
import com.rybka.model.BankData;
import com.rybka.model.dto.BankResponse;
import com.rybka.util.RateCalculator;
import coresearch.cvurl.io.request.CVurl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
@RequiredArgsConstructor
public class BankAggregatorService {

    private final CVurl cVurl;
    private String DATE_PATTERN = "yyyy-MM-dd HH:mm";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public BankResponse loadExchangeRatesWithSummary(String currency) {
        var banks = loadExchangeRatesPageFor(currency);
        var summary = RateCalculator.calculateStatisticalRate(banks);

        return new BankResponse(banks, summary);
    }

    private List<BankData> loadExchangeRatesPageFor(String currency) {
        try {
            var document = Jsoup.parse(retrieveHTMLContent(currency));
            var trElementRowList = document.select(BankAggregatorConstants.BANK_TABLE_ROW);

            return trElementRowList.stream().map(this::parseBankDataRow).collect(Collectors.toList());
        } catch (Exception exception) {
            log.error(Messages.RESOURCE_PROCESSING_EXCEPTION_MSG + exception.getMessage());
            throw new ResourceProcessingException(Messages.RESOURCE_PROCESSING_EXCEPTION_MSG + exception.getMessage());
        }
    }

    private String retrieveHTMLContent(String currency) {
        return cVurl
                .get((String.format(BankAggregatorConstants.BANK_AGGREGATOR_URL, currency)))
                .asString()
                .orElseThrow(() -> new InvalidResponseException(Messages.RESPONSE_EXCEPTION_MSG))
                .getBody();
    }

    private BankData parseBankDataRow(Element trElement) {
        var bankName = trElement.select(BankAggregatorConstants.BANK_HEADER).text();
        var bankLink = BankAggregatorConstants.BANK_ROOT_URL + trElement.select(BankAggregatorConstants.BANK_LINK)
                .select(BankAggregatorConstants.BANK_LINK_ATTRIBUTE).attr(BankAggregatorConstants.BANK_LINK_HREF);
        var bankBuyRate = Double.parseDouble(trElement.select(BankAggregatorConstants.BANK_BUY_RATE).text());
        var bankSellRate = Double.parseDouble(trElement.select(BankAggregatorConstants.BANK_SELL_RATE).text());
        var bankLastUpdateDate = LocalDateTime.parse(trElement.select(BankAggregatorConstants.BANK_LAST_UPDATE_DATE).text()
                .replace(BankAggregatorConstants.BANK_DATE_SEPARATOR_OLD, BankAggregatorConstants.BANK_DATE_SEPARATOR_NEW), formatter);

        return new BankData(bankName, bankLink, bankBuyRate, bankSellRate, bankLastUpdateDate);
    }
}
