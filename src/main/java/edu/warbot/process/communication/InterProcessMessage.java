package edu.warbot.process.communication;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by beugnon on 21/04/15.
 * <p>
 * Classe abstraite pour la création des messages interprocessus pour
 * Warbot
 *
 * @author beugnon
 */
public class InterProcessMessage implements Serializable {

    /**
     * Entête du message
     */
    private final String header;

    /**
     * Date d'envoi du message
     */
    private final Date date;

    /**
     * Constructeur
     *
     * @param header Entête du message inter-processus
     */
    public InterProcessMessage(String header) {
        this.header = header;
        this.date = new Date();
    }

    public String getHeader() {
        return header;
    }

    public Date getDate() {
        return date;
    }
}
