package com.rybka.command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Invoker {
    private final Command convertCommand;

    public void convert() {
        convertCommand.execute();
    }
}
