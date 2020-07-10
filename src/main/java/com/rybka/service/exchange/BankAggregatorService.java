package com.rybka.service.exchange;

import com.rybka.constant.BankAggregatorConstants;
import com.rybka.model.BankData;
import lombok.Data;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
@Data
public class BankAggregatorService {

    @SneakyThrows
    public void loadExchangeRatesFor(String currency) {
        var bankExchangePage = Jsoup.connect(String.format(BankAggregatorConstants.BANK_AGGREGATOR_URL, currency)).get();
        var bankDataList = parseExchangeRatesPage(bankExchangePage);
    }

    @SneakyThrows
    private List<BankData> parseExchangeRatesPage(Document document) {
        var exchangeTableBody = document.getElementsByClass(BankAggregatorConstants.AGGREGATOR_TABLE_BODY);
        var bankHeaderList = exchangeTableBody.select(BankAggregatorConstants.BANK_HEADER).eachText();
        var bankLinkList = exchangeTableBody.select(BankAggregatorConstants.BANK_LINK)
                .select(BankAggregatorConstants.BANK_LINK_ATTRIBUTE).eachAttr(BankAggregatorConstants.BANK_LINK_HREF);
        var bankBuyList = exchangeTableBody.select(BankAggregatorConstants.BANK_BUY_RATE).eachText();
        var bankSellList = exchangeTableBody.select(BankAggregatorConstants.BANK_SELL_RATE).eachText();
        var bankUpdateDateList = exchangeTableBody.select(BankAggregatorConstants.BANK_LAST_UPDATE_DATE).eachText();
        var formatter = new SimpleDateFormat(BankAggregatorConstants.DATE_PATTERN);

        List<BankData> bankDataList = new java.util.ArrayList<>();

        for (int i = 0; i < bankHeaderList.size(); i++) {
            bankDataList.add(new BankData(bankHeaderList.get(i), bankLinkList.get(i),
                    Double.parseDouble(bankBuyList.get(i)),
                    Double.parseDouble(bankSellList.get(i)),
                    formatter.parse(bankUpdateDateList.get(i).replace('.', '-'))));
        }

        return bankDataList;
    }
}
