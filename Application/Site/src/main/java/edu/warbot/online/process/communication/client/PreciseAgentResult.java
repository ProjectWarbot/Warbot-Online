package edu.warbot.online.process.communication.client;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.online.process.communication.InterProcessMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beugnon on 21/04/15.
 *
 * @author beugnon
 */
public class PreciseAgentResult extends InterProcessMessage {
    /**
     * Constructeur
     */

    public final static String HEADER = "PreciseAgentResult";

    private Map<String, Serializable> statistics;

    public PreciseAgentResult(ControllableWarAgent agent) {
        super(HEADER);
        this.statistics = new HashMap<String, Serializable>();
        this.statistics.put("bagSize", agent.getBagSize());
        this.statistics.put("x", agent.getX());
        this.statistics.put("y", agent.getY());
        this.statistics.put("type", agent.getType());
        this.statistics.put("team", agent.getTeamName());
        this.statistics.put("messageDebug", agent.getDebugString());
        this.statistics.put("currentBagSize", agent.getNbElementsInBag());
        this.statistics.put("heading", agent.getHeading());
        this.statistics.put("health", agent.getHealth());
        this.statistics.put("maxHealth", agent.getMaxHealth());
        this.statistics.put("viewDistance", agent.getDistanceOfView());
        this.statistics.put("viewAngle", agent.getAngleOfView());
        this.statistics.put("viewHeading", agent.getViewDirection());

        ArrayList<String> percepts = new ArrayList<String>();
        for (WarAgentPercept wp : agent.getPercepts()) {
            percepts.add(
                    new String(wp.getTeamName() + ":" + wp.getType() + "-" + wp.getID()));
        }

        this.statistics.put("percepts", percepts);

    }

    public Map<String, Serializable> getStatistics() {
        return this.statistics;
    }

    public void setStatistics(Map<String, Serializable> statistics) {
        this.statistics = statistics;
    }
}
