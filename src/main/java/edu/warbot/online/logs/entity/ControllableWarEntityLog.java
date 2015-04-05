package edu.warbot.online.logs.entity;

import edu.warbot.agents.ControllableWarAgent;

import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 */
public class ControllableWarEntityLog extends EntityLog {
    private double lifeP;


    public ControllableWarEntityLog(String name) {
        super(name);
    }

    @Override
    public Map<String, Object> update(ControllableWarAgent wa) {
        Map<String, Object> map = super.update(wa);

        double lifePercent = computeLifePercent(wa.getHealth(), wa.getMaxHealth());

        if (lifeP != lifePercent) {
            lifeP = lifePercent;
            map.put("lifeP", lifeP);
        }

        return map;
    }

    @Override
    public Map<String, Object> getCurrentState() {
        Map<String, Object> map = super.getCurrentState();
        map.put("lifeP", lifeP);
        return map;
    }

    public double computeLifePercent(double health, double healthMax) {
        return (health / healthMax) * 100.0;
    }
}

