package edu.warbot.online.messaging;

import java.util.Map;

/**
 * Created by beugnon on 06/04/15.
 */
public class InitMessage extends ClassicMessage {
    private final Map<String, Object> content;

    public InitMessage(Map<String, Object> content) {
        super("init");
        this.content = content;
    }

    public Map<String, Object> getContent() {
        return content;
    }
}
