package edu.warbot.process.communication.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 06/04/15.
 */
public class EndMessage extends InterProcessMessage {
    private Object content;

    public final static String HEADER = "end";

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
