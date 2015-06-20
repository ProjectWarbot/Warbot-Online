package edu.warbot.online.process.communication.client;

import edu.warbot.online.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 21/04/15.
 */
public class GameLogResult extends InterProcessMessage {
    /**
     * Constructeur
     *
     * @param header Entête du message inter-processus
     */
    public GameLogResult(String header) {
        super(header);
    }
}
