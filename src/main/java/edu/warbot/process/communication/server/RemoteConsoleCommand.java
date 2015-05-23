package edu.warbot.process.communication.server;

import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 21/04/15.
 */
public class RemoteConsoleCommand extends InterProcessMessage {

    public final static String HEADER = "RemoteConsoleCommand";

    private final ConsoleCommand consoleCommand;

    public RemoteConsoleCommand(ConsoleCommand cc) {
        super(RemoteConsoleCommand.class.getSimpleName());
        this.consoleCommand = cc;
    }

    public ConsoleCommand getConsoleCommand() {
        return consoleCommand;
    }

    public enum ConsoleCommand {OUT_ON, OUT_OFF, ERR_ON, ERR_OFF}
}
