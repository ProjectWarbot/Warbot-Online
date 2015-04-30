package edu.warbot.models;

import edu.warbot.agents.enums.WarAgentType;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * Created by quent on 23/04/2015.
 */
@Entity
@Table(name = "TRAINING_AGENT")
@NamedQuery(name = TrainingAgent.FIND_BY_NAME,query = "select m from TrainingAgent m where m.name = :name")
public class TrainingAgent extends AbstractPersistable<Long> {

    public static final String FIND_BY_NAME = "TrainingAgent.findByName";


    @Column(name = "trainingAgent_name", unique = true)
    private String name;

    @Column(name = "trainingAgent_x", unique = false)
    private double x;

    @Column(name = "trainingAgent_y", unique = false)
    private double y;

    @Column(name = "trainingAgent_angle", unique = false)
    private double angle;

    @Column(name = "trainingAgent_team", unique = false)
    private String teamName;

    @Column(name = "trainingAgent_type")
    private WarAgentType type;

    @Column(name="trainingAgent_life", unique = false)
    private double life;

}
