package edu.warbot.process.communication.client;

import edu.warbot.process.communication.InterProcessMessage;

import java.util.HashMap;

/**
 * Created by beugnon on 06/04/15.
 */
public class EndMessage extends InterProcessMessage {
    public final static String HEADER = "end";

    private HashMap<String, String> supp = new HashMap<>();

    private Object content;

    public EndMessage() {

    }

    public EndMessage(Object content) {
        super(HEADER);
        this.content = content;
    }

    public EndMessage(Object content, HashMap<String, String> map) {
        super(HEADER);
        this.content = content;
        this.supp = map;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public HashMap<String, String> getSupp() {
        return supp;
    }

    public void setSupp(HashMap<String, String> supp) {
        this.supp = supp;
    }
}
