package edu.warbot.online.logs;


import java.io.IOException;

/**
 * Created by beugnon on 04/04/15.
 */
public class RGB
{
    private int r;

    private int g;

    private int b;

    public RGB(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }


    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
