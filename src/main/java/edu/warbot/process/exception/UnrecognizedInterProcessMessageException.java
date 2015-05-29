package edu.warbot.process.exception;

/**
 * Created by beugnon on 30/04/15.
 */
public class UnrecognizedInterProcessMessageException extends Throwable {
    public UnrecognizedInterProcessMessageException(String header) {
        super("Message with header : " + header + " unhandled");
    }
}
