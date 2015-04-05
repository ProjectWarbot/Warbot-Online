package edu.warbot.online.logs;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by beugnon on 04/04/15.
 */
public class RGB
{
    public int r;

    public int g;

    public int b;

    public RGB(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
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
