package edu.warbot.process.communication.server;

import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 21/04/15.
 */
public class RemoteWarbotCommand extends InterProcessMessage {

    public final static String HEADER = "RemoteWarbotCommand";

    private final WarbotCommand warbotCommand;

    public RemoteWarbotCommand(WarbotCommand warbotCommand) {
        super(RemoteWarbotCommand.class.getSimpleName());
        this.warbotCommand = warbotCommand;
    }

    ;

    public WarbotCommand getWarbotCommand() {
        return warbotCommand;
    }

    public static enum WarbotCommand {PAUSE, RESTART, STOP}
}
