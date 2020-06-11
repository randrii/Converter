package com.rybka.view;

import com.rybka.command.Command;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class ExchangeView {

    private final Command convertCommand;
    private final Command historyCommand;

    public void showView() {
        convertCommand.execute();
        historyCommand.execute();
    }

}
