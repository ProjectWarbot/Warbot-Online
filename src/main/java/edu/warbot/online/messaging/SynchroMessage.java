package edu.warbot.online.messaging;

import java.util.Collection;
import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 *
 * Classe représentant un message de synchronisation.
 *
 * @author beugnon
 */
public class SynchroMessage extends ClassicMessage
{

    private final Collection<Map<String, Object>> content;

    public SynchroMessage(String header,
                          Collection<Map<String,Object>>
                                  content)
    {
        super("synchro");
        this.content = content;
    }

    public Collection<Map<String, Object>> getContent() {
        return content;
    }
}
