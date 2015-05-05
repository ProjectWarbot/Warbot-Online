package edu.warbot.process.communication.client;

import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 06/04/15.
 */
public class EndMessage extends InterProcessMessage {
    private final Object content;

    public final static String HEADER = "end";

    public EndMessage(Object content) {
        super(HEADER);
        this.content = content;
    }


    public Object getContent() {
        return content;
    }
}
