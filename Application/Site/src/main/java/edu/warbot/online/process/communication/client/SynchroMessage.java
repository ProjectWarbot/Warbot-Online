package edu.warbot.online.process.communication.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.warbot.online.process.communication.InterProcessMessage;

import java.util.Collection;
import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 * <p/>
 * Classe représentant un message de synchronisation.
 *
 * @author beugnon
 */
public class SynchroMessage extends InterProcessMessage {

    public final static String HEADER = "synchro";
    private final Collection<Map<String, Object>> content;

    public SynchroMessage(String header,
                          Collection<Map<String, Object>>
                                  content) {
        super(HEADER);
        this.content = content;
    }

    @JsonIgnore
    public Collection<Map<String, Object>> getContent() {
        return content;
    }
}
