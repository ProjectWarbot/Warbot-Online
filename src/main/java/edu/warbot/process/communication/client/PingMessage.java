package edu.warbot.process.communication.client;

import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 30/04/15.
 */
public class PingMessage extends InterProcessMessage {

    public static final String HEADER = "PingMessage";

    public boolean answer;
    /**
     * Constructeur
     *
     */
    public PingMessage() {
        super(HEADER);
        answer = false;
    }

    public PingMessage(boolean answer) {
        super(HEADER);
        this.answer = answer;
    }

    public boolean isPingAnswer() {
        return this.answer;
    }
}
