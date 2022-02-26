package edu.ifmo.math;

import picocli.CommandLine;

public class PrintExceptionMessageHandler implements CommandLine.IExecutionExceptionHandler {
    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, CommandLine.ParseResult parseResult) {
        // red text
        commandLine.getErr().println(commandLine.getColorScheme().errorText(ex.getMessage()));

        return commandLine.getExitCodeExceptionMapper() != null
            ? commandLine.getExitCodeExceptionMapper().getExitCode(ex)
            : commandLine.getCommandSpec().exitCodeOnExecutionException();
    }
}
