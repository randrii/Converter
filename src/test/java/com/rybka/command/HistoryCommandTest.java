package com.rybka.command;

import com.rybka.repository.CurrencyHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
public class HistoryCommandTest {
    @Mock
    private CurrencyHistoryRepository repository;
    @InjectMocks
    private HistoryCommand command;

    @Test
    public void testHistoryExecution() {

        // given
        Mockito.when(repository.findTop5ByOrderByIdDesc()).thenReturn(List.of());

        // when
        command.execute();

        //then
        Mockito.verify(repository).findTop5ByOrderByIdDesc();
    }
}