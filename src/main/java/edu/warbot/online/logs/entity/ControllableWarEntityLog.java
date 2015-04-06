package edu.warbot.online.logs.entity;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.online.logs.RGB;

import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 *
 * @author beugnon
 */
public class ControllableWarEntityLog extends EntityLog {
    private double lifeP;

    private RGB colorDebug;

    private String messageDebug;

    public ControllableWarEntityLog(String name) {
        super(name);
        this.messageDebug = null;
        colorDebug = new RGB(0,0,0);
    }

    public Map<String, Object> update(ControllableWarAgent wa) {
        Map<String, Object> map = super.update(wa);

        double lifePercent = computeLifePercent(wa.getHealth(), wa.getMaxHealth());

        if (lifeP != lifePercent) {
            lifeP = lifePercent;
            map.put("lifeP", lifeP);
        }

        if(wa.getDebugStringColor() != null
                && (colorDebug.getR() != wa.getDebugStringColor().getRed()
                || colorDebug.getG() != wa.getDebugStringColor().getGreen()
                || colorDebug.getB() != wa.getDebugStringColor().getBlue())
                )
        {
            colorDebug.setR(wa.getDebugStringColor().getRed());
            colorDebug.setG(wa.getDebugStringColor().getGreen());
            colorDebug.setB(wa.getDebugStringColor().getBlue());
            map.put("colorDebug",colorDebug.toString());
        }

        if(wa.getDebugString()!= null && !wa.getDebugString().equals(messageDebug))
        {
            messageDebug = wa.getDebugString();
            map.put("messageDebug",messageDebug);
        }

        return map;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        Map<String, Object> map = super.getCurrentState();
        map.put("lifeP", lifeP);
        if(colorDebug!=null)
            map.put("colorDebug",colorDebug.toString());
        if(messageDebug!=null)
            map.put("messageDebug",messageDebug);
        return map;
    }

    public double computeLifePercent(double health, double healthMax) {
        return (health / healthMax) * 100.0;
    }
}

