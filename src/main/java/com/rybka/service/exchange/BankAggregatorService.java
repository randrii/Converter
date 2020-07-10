package com.rybka.service.exchange;

import com.rybka.constant.BankAggregatorConstants;
import com.rybka.constant.Messages;
import com.rybka.exception.AggregatorConnectionException;
import com.rybka.exception.ParameterParseException;
import com.rybka.model.BankData;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
@RequiredArgsConstructor
public class BankAggregatorService {

    private final HttpClient httpClient;
    private final String DATE_PATTERN = "yyyy-M-dd hh:mm";

    public List<BankData> loadExchangeRatesPageFor(String currency) {
        try {
            var request = HttpRequest.newBuilder()
                    .uri(URI.create((String.format(BankAggregatorConstants.BANK_AGGREGATOR_URL, currency))))
                    .build();
            var response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            var document = Jsoup.parse(response.body());
            var trElementRowList = document.select(BankAggregatorConstants.BANK_TABLE_ROW);

            return trElementRowList.stream().map(this::parseBankDataRow).collect(Collectors.toList());
        } catch (IOException | InterruptedException e) {
            log.error(Messages.LOG_AGGREGATOR_EXCEPTION_MSG + e.getMessage());
            throw new AggregatorConnectionException(Messages.LOG_AGGREGATOR_EXCEPTION_MSG + e.getMessage());
        }
    }

    private BankData parseBankDataRow(Element trElement) {
        var bankName = trElement.select(BankAggregatorConstants.BANK_HEADER).text();
        var bankLink = trElement.select(BankAggregatorConstants.BANK_LINK)
                .select(BankAggregatorConstants.BANK_LINK_ATTRIBUTE).attr(BankAggregatorConstants.BANK_LINK_HREF);
        var bankBuyRate = trElement.select(BankAggregatorConstants.BANK_BUY_RATE).text();
        var bankSellRate = trElement.select(BankAggregatorConstants.BANK_SELL_RATE).text();
        var bankLastUpdateDate = trElement.select(BankAggregatorConstants.BANK_LAST_UPDATE_DATE).text();

        return buildObject(bankName, bankLink, bankBuyRate, bankSellRate, bankLastUpdateDate);
    }

    private BankData buildObject(String bankName, String bankLink, String bankBuyRate, String bankSellRate, String bankLastUpdateDate) {
        var formatter = new SimpleDateFormat(DATE_PATTERN);

        try {
            return new BankData(bankName, bankLink, Double.parseDouble(bankBuyRate), Double.parseDouble(bankSellRate),
                    formatter.parse(bankLastUpdateDate.replace('.', '-')));
        } catch (ParseException e) {
            log.error(Messages.LOG_BANK_DATA_EXCEPTION_MSG + e.getMessage());
            throw new ParameterParseException(Messages.LOG_BANK_DATA_EXCEPTION_MSG + e.getMessage());
        }
    }
}
