package edu.warbot.process.communication.client;

import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 06/04/15.
 */
public class EndMessage extends InterProcessMessage {
    public final static String HEADER = "end";
    private Object content;

    public EndMessage() {

    }

    public EndMessage(Object content) {
        super(HEADER);
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
