package com.rybka.service.exchange;

import com.rybka.constant.BankAggregatorConstants;
import com.rybka.exception.ResourceProcessingException;
import com.rybka.model.BankData;
import coresearch.cvurl.io.model.Response;
import coresearch.cvurl.io.request.CVurl;
import coresearch.cvurl.io.request.RequestBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BankAggregatorServiceTest {
    @Mock
    private CVurl cVurl;
    @Mock
    private RequestBuilder requestBuilder;
    @Mock
    private Optional<Response<String>> stringResponse;
    @Mock
    private Response<String> response;
    @InjectMocks
    private BankAggregatorService service;

    @Test
    public void testOnProperWork() {

        // given
        var bankAggregatorPage = "bankAggregatorPage.html";
        var bankAggregatorContent = retrieveResource(bankAggregatorPage);
        var targetCurrency = "usd";
        var datePattern = "yyyy-MM-dd HH:mm";
        var lastUpdatedDateString = "2020-07-11 08:15";
        var formatter = DateTimeFormatter.ofPattern(datePattern);
        var lastUpdatedDate = LocalDateTime.parse(lastUpdatedDateString, formatter);
        var expectedBankDataList = List.of(
                buildBankData("Приватбанк", "/ua/company/privatbank/", 26.8, 27.25, lastUpdatedDate),
                buildBankData("Райффайзен Банк Аваль", "/ua/company/aval/", 26.7, 27.15, lastUpdatedDate),
                buildBankData("Альфа-Банк", "/ua/company/alfa-bank/", 26.92, 27.17, lastUpdatedDate));

        when(cVurl.get((String.format(BankAggregatorConstants.BANK_AGGREGATOR_URL, targetCurrency)))).thenReturn(requestBuilder);
        when(requestBuilder.asString()).thenReturn(stringResponse);
        when(stringResponse.get()).thenReturn(response);
        when(response.getBody()).thenReturn(bankAggregatorContent);

        // when
        var bankDataList = service.loadExchangeRatesPageFor(targetCurrency);

        // then
        Assertions.assertEquals(expectedBankDataList, bankDataList);
    }

    @Test
    public void testOnBrokenResponse() {

        // given
        var brokenResponseBody = "";
        var targetCurrency = "usd";
        Response<String> nullResponse = null;

        when(cVurl.get((String.format(BankAggregatorConstants.BANK_AGGREGATOR_URL, targetCurrency)))).thenReturn(requestBuilder);
        when(requestBuilder.asString()).thenReturn(stringResponse);
        when(stringResponse.get()).thenReturn(nullResponse);
        when(response.getBody()).thenReturn(brokenResponseBody);

        // then
        Assertions.assertThrows(ResourceProcessingException.class, () -> service.loadExchangeRatesPageFor(targetCurrency));
    }

    private BankData buildBankData(String name, String link, double buy, double sell, LocalDateTime lastUpdated) {
        return new BankData(name, link, buy, sell, lastUpdated);
    }

    @SneakyThrows
    private String retrieveResource(String fileName) {
        var testFile = new ClassPathResource(fileName);

        return Files.readString(testFile.getFile().toPath());
    }
}