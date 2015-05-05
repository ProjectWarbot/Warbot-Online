package edu.warbot.process.communication.client;

import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 21/04/15.
 */
public class ExceptionResult extends InterProcessMessage {

    private Exception exception;

    public final static String HEADER = "ExceptionResult";


    public ExceptionResult(Exception exception) {
        super(HEADER);
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
