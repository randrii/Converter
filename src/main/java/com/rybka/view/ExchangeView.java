package com.rybka.view;

import com.rybka.command.Command;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExchangeView {
    private final Command convertCommand;
    private final Command historyCommand;
    private final Command exportCommand;

    public void showView() {

    }

}
