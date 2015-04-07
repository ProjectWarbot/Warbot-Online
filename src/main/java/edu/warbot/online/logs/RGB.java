package edu.warbot.online.logs;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 *
 * @author beugnon
 */
public class RGB implements Serializable
{
    private Map<String,Integer> rgb;

    public RGB(int r, int g, int b)
    {
        this.rgb = new HashMap<>();
        rgb.put("r",r);
        rgb.put("g",g);
        rgb.put("b",b);
    }


    public int getR() {
        return rgb.get("r");
    }

    public void setR(int r) {
        this.rgb.put("r",r);
    }

    public int getG() {
        return rgb.get("g");
    }

    public void setG(int g) {
        this.rgb.put("g",g);
    }

    public int getB() {
        return rgb.get("b");
    }

    public void setB(int b) {
        this.rgb.put("b",b);
    }
}
