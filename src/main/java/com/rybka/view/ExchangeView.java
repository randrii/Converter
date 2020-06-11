package com.rybka.view;

import com.rybka.command.Command;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor
public class ExchangeView {

    private final Command convertCommand;

    public void showView() {
        convertCommand.execute();
    }

}
