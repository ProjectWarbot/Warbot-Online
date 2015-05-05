package edu.warbot.process.communication.client;

import edu.warbot.process.communication.InterProcessMessage;

import java.util.Map;

/**
 * Created by beugnon on 06/04/15.
 */
public class InitMessage extends InterProcessMessage {
    private final Map<String, Object> content;

    public final static String HEADER = "init";


    public InitMessage(Map<String, Object> content) {
        super(HEADER);
        this.content = content;
    }

    public Map<String, Object> getContent() {
        return content;
    }
}
