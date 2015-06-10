package edu.warbot.process.communication;

import java.io.Serializable;

/**
 * Created by beugnon on 21/04/15.
 * <p/>
 * Classe abstraite pour la création des messages interprocessus pour
 * Warbot
 *
 * @author beugnon
 */
public class InterProcessMessage implements Serializable {

    /**
     * Entête du message
     */
    private String header;


    public InterProcessMessage() {
        this.header = "none";
    }

    /**
     * Constructeur
     *
     * @param header Entête du message inter-processus
     */
    public InterProcessMessage(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

}
