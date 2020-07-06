package com.rybka.command;

import com.rybka.exception.IncorrectUserDataException;
import com.rybka.model.CurrencyData;
import com.rybka.model.CurrencyHistory;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.service.exchange.ExchangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ConvertCommandTest {
    @Mock
    private Scanner scanner;
    @Mock
    private ExchangeService exchangeService;
    @Mock
    private CurrencyHistoryRepository currencyHistoryRepository;
    @Captor
    private ArgumentCaptor<CurrencyHistory> historyArgumentCaptor;
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
        verify(currencyHistoryRepository).save(historyArgumentCaptor.capture());

        assertEquals(baseCurrency, historyArgumentCaptor.getValue().getBase());
        assertEquals(targetCurrency, historyArgumentCaptor.getValue().getTarget());
        assertEquals(currencyRate, historyArgumentCaptor.getValue().getRate());
        assertEquals(currencyAmount, historyArgumentCaptor.getValue().getAmount());
        assertEquals(currencyRate * currencyAmount, historyArgumentCaptor.getValue().getTotal());
    }

    @Test
    public void testOnUserDataException() {

        //given
        var baseCurrency = "USD";
        var targetCurrency = "EUR";
        var currencyAmount = 5d;
        var currencyRate = 0.8877;
        var currencyData = new CurrencyData(baseCurrency, targetCurrency, currencyRate);
        var currencyHistory = new CurrencyHistory();

        when(scanner.next()).thenReturn(baseCurrency).thenReturn(targetCurrency);
        when(scanner.nextDouble()).thenThrow(IncorrectUserDataException.class);
        when(exchangeService.loadCurrencyOf(baseCurrency, targetCurrency)).thenReturn(currencyData);
        when(exchangeService.calculateTotal(currencyRate, currencyAmount)).thenReturn(currencyRate * currencyAmount);
        when(currencyHistoryRepository.save(Mockito.any(CurrencyHistory.class))).thenReturn(currencyHistory);

        // when
        command.execute();

        // then
        verify(scanner).next();
        verify(scanner).nextDouble();
        verifyNoInteractions(exchangeService);
        verifyNoInteractions(exchangeService);
        verifyNoInteractions(currencyHistoryRepository);
    }
}