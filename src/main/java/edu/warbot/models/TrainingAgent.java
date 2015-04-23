package edu.warbot.models;

import edu.warbot.agents.enums.WarAgentType;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * Created by quent on 23/04/2015.
 */
public class TrainingAgent {

    @Column(name = "trainingAgent_name", unique = false)
    private String name;

    @Column(name = "trainingAgent_x", unique = false)
    private double x;

    @Column(name = "trainingAgent_y", unique = false)
    private double y;

    @Column(name = "trainingAgent_angle", unique = false)
    private double angle;

    @Column(name = "trainingAgent_team", unique = false)
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = WarAgentType.class)
    private WarAgentType type;

    @Column(name="trainingAgent_life", unique = false)
    private double life;

}
