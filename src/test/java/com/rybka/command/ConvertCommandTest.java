package com.rybka.command;

import com.rybka.model.CurrencyData;
import com.rybka.model.CurrencyHistory;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.service.exchange.ExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Scanner;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ConvertCommandTest {
    @Mock
    private Scanner scanner;
    @Mock
    private ExchangeService exchangeService;
    @Mock
    private CurrencyHistoryRepository currencyHistoryRepository;
    @InjectMocks
    private ConvertCommand command;

    @Test
    public void testOnConvertExecution() {

        //given
        var baseCurrency = "USD";
        var targetCurrency = "EUR";
        var currencyAmount = 5d;
        var currencyRate = 0.8877;
        var currencyData = new CurrencyData(baseCurrency, targetCurrency, currencyRate);
        var currencyHistory = new CurrencyHistory();

        when(scanner.next()).thenReturn(baseCurrency).thenReturn(targetCurrency);
        when(scanner.nextDouble()).thenReturn(currencyAmount);
        when(exchangeService.loadCurrencyOf(baseCurrency, targetCurrency)).thenReturn(currencyData);
        when(exchangeService.calculateTotal(currencyRate, currencyAmount)).thenReturn(currencyRate * currencyAmount);
        when(currencyHistoryRepository.save(Mockito.any(CurrencyHistory.class))).thenReturn(currencyHistory);

        // when
        command.execute();

        // then
        verify(scanner, times(2)).next();
        verify(scanner, times(1)).nextDouble();
        verify(exchangeService).loadCurrencyOf(baseCurrency, targetCurrency);
        verify(exchangeService).calculateTotal(currencyRate, currencyAmount);
        verify(currencyHistoryRepository).save(Mockito.any(CurrencyHistory.class));
    }
}