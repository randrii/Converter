package com.rybka.service.exchange;

import com.rybka.constant.BankAggregatorConstants;
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
        var expectedBankDataListSize = 62;

        when(cVurl.get((String.format(BankAggregatorConstants.BANK_AGGREGATOR_URL, targetCurrency)))).thenReturn(requestBuilder);
        when(requestBuilder.asString()).thenReturn(stringResponse);
        when(stringResponse.get()).thenReturn(response);
        when(response.getBody()).thenReturn(bankAggregatorContent);

        // when
        var bankDataList = service.loadExchangeRatesPageFor(targetCurrency);

        // then
        Assertions.assertEquals(expectedBankDataListSize, bankDataList.size());
    }

    @SneakyThrows
    private String retrieveResource(String fileName) {
        var testFile = new ClassPathResource(fileName);

        return Files.readString(testFile.getFile().toPath());
    }
}