package edu.warbot.online.online.logs;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 *
 * @author beugnon
 */
public class RGB implements Serializable {
    int r;
    int g;
    int b;

    public RGB(int r, int g, int b) {
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
        return this.g;
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


    @Override
    public String toString() {
        return "{'r':" + r + "," +
                "'g':" + g + "," +
                "'b':" + b + "}";
    }

    /**
     * @return une association de couleur rouge, vert, bleu
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("r", r);
        map.put("g", g);
        map.put("b", b);
        return map;
    }
}
