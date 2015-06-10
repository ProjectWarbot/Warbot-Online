package edu.warbot.online.process.communication.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.warbot.online.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 21/04/15.
 */
public class ExceptionResult extends InterProcessMessage {

    public final static String HEADER = "ExceptionResult";
    private final ExceptionLevel level;
    private Exception exception;

    public ExceptionResult(Exception exception) {
        super(HEADER);
        this.exception = exception;
        this.level = ExceptionLevel.GRAVE;
    }


    public ExceptionResult(Exception exception, ExceptionLevel level) {
        super(HEADER);
        this.exception = exception;
        this.level = level;
    }

    @JsonIgnore
    public ExceptionLevel getLevel() {
        return level;
    }

    @JsonIgnore
    public Exception getException() {
        return exception;
    }

    public enum ExceptionLevel {GRAVE, WARNING}
}
