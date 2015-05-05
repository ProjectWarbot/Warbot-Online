package edu.warbot.process.communication.client;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.process.communication.InterProcessMessage;

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

    private Map<String, Serializable> association;

    public PreciseAgentResult(ControllableWarAgent agent) {
        super(HEADER);
        this.association = new HashMap<String, Serializable>();
        this.association.put("bagSize", agent.getBagSize());
        this.association.put("x", agent.getX());
        this.association.put("y", agent.getY());
        this.association.put("type", agent.getType());
        this.association.put("team", agent.getTeamName());
        this.association.put("messageDebug", agent.getDebugString());
        this.association.put("currentBagSize", agent.getNbElementsInBag());
        this.association.put("heading", agent.getHeading());
        this.association.put("health", agent.getHealth());
        this.association.put("maxHealth", agent.getMaxHealth());
        this.association.put("viewDistance", agent.getDistanceOfView());
        this.association.put("viewAngle", agent.getAngleOfView());
        this.association.put("viewHeading", agent.getViewDirection());

        ArrayList<String> percepts = new ArrayList<String>();
        for (WarAgentPercept wp : agent.getPercepts()) {
            percepts.add(
                    new String(wp.getTeamName() + ":" + wp.getType() + "-" + wp.getID()));
        }

        this.association.put("percepts", percepts);

    }

    public Map<String, Serializable> getAssociation() {
        return this.association;
    }
}
