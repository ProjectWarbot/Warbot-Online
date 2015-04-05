package edu.warbot.online.messaging;

import org.mule.mvel2.util.Make;

import java.util.Collection;

/**
 * Created by beugnon on 04/04/15.
 *
 * Classe représentant un message de synchronisation.
 *
 * @author beugnon
 */
public class SynchroMessage extends ClassicMessage
{

    public SynchroMessage(String header,
                          Collection<Make.Map<String,Object>>
                                  content)
    {
        super("synchro", content);
    }
}
