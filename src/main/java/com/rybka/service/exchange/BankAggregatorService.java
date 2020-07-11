package com.rybka.service.exchange;

import com.rybka.constant.BankAggregatorConstants;
import com.rybka.constant.Messages;
import com.rybka.exception.ParameterParseException;
import com.rybka.model.BankData;
import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
@RequiredArgsConstructor
public class BankAggregatorService {

    private final CVurl cVurl;
    private final String DATE_PATTERN = "yyyy-M-dd hh:mm";

    public List<BankData> loadExchangeRatesPageFor(String currency) {
        var document = Jsoup.parse(retrieveResourcePage(currency).getBody());
        var trElementRowList = document.select(BankAggregatorConstants.BANK_TABLE_ROW);

        return trElementRowList.stream().map(this::parseBankDataRow).collect(Collectors.toList());
    }

    private Response<String> retrieveResourcePage(String currency) {
        return cVurl
                .get((String.format(BankAggregatorConstants.BANK_AGGREGATOR_URL, currency)))
                .asString().get();
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
