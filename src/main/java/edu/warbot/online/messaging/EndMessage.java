package edu.warbot.online.messaging;

/**
 * Created by beugnon on 06/04/15.
 */
public class EndMessage extends ClassicMessage
{
    private final Object content;

    public EndMessage(Object content) {
        super("end");
        this.content = content;
    }


    public Object getContent() {
        return content;
    }
}
