package com.rybka.command;

import com.rybka.constant.CommandConstants;
import com.rybka.constant.Messages;
import com.rybka.repository.CurrencyHistoryRepository;
import com.rybka.util.FormatPrintUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component(CommandConstants.HISTORY_COMMAND)
@Slf4j
@RequiredArgsConstructor
public class HistoryCommand implements Command {
    private final CurrencyHistoryRepository currencyHistoryRepository;

    @Override
    public void execute() {
        try {
            var histories = currencyHistoryRepository.findTop5ByOrderByDateDesc();

            FormatPrintUtil.printHistory(histories);
        } catch (Exception exception) {
            log.error(Messages.LOG_HISTORY_ERROR_MSG + exception.getMessage());
        }
    }
}
