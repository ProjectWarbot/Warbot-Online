package edu.warbot.online.messaging;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by beugnon on 04/04/15.
 *
 * Classe représentant un message JSON pour la communication avec le client JS
 * de Warbot-Online.
 *
 * @author beugnon
 */
public class ClassicMessage
{
    private String header;

    private Object content;

    public ClassicMessage(String header, Object content)
    {
        this.header = header;
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        try {
            return om.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
