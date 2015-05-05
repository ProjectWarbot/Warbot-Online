package edu.warbot.process.communication.client;

import edu.warbot.process.communication.InterProcessMessage;

import java.util.Collection;
import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 * <p>
 * Classe représentant un message de synchronisation.
 *
 * @author beugnon
 */
public class SynchroMessage extends InterProcessMessage {

    private final Collection<Map<String, Object>> content;
    public final static String HEADER = "synchro";

    public SynchroMessage(String header,
                          Collection<Map<String, Object>>
                                  content) {
        super(HEADER);
        this.content = content;
    }

    public Collection<Map<String, Object>> getContent() {
        return content;
    }
}
