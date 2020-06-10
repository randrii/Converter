package com.rybka.command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConvertCommand implements Command {
    private final ConvertTask convertTask;

    @Override
    public void execute() {
        var convertedResult = convertTask.convert();
        convertTask.print(convertedResult);
        convertTask.save(convertedResult);
    }

}
