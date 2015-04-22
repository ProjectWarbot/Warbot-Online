package edu.warbot.online.messaging;


import java.io.IOException;

/**
 * Created by beugnon on 04/04/15.
 *
 * Classe représentant un message JSON pour la communication avec le client JS
 * de Warbot-Online.
 *
 * @author beugnon
 */
public class ClassicMessage {
    private String header;


    public ClassicMessage(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

}
