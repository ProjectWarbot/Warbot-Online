package edu.warbot.process.communication.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 21/04/15.
 */
public class ExceptionResult extends InterProcessMessage {

    private final ExceptionLevel level;

    private Exception exception;

    public enum ExceptionLevel {GRAVE, WARNING}

    public final static String HEADER = "ExceptionResult";


    public ExceptionResult(Exception exception) {
        super(HEADER);
        this.exception = exception;
        this.level = ExceptionLevel.GRAVE;
    }

    public ExceptionResult(Exception exception,ExceptionLevel level) {
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
}
