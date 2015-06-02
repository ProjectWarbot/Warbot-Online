package edu.warbot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.warbot.agents.enums.WarAgentType;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * Created by quent on 23/04/2015.
 */
@Entity
@Table(name = "TRAINING_AGENT")
@NamedQuery(name = TrainingAgent.FIND_BY_NAME, query = "select m from TrainingAgent m where m.name = :name")
public class TrainingAgent extends AbstractPersistable<Long> {

    public static final String FIND_BY_NAME = "TrainingAgent.findByName";


    @Column(name = "trainingAgent_name")
    private String name;

    @Column(name = "trainingAgent_x")
    private double x;

    @Column(name = "trainingAgent_y")
    private double y;

    @Column(name = "trainingAgent_angle")
    private double angle;

    @Column(name = "trainingAgent_team")
    private String teamName;

    @Column(name = "trainingAgent_type")
    private WarAgentType type;

    @Column(name = "trainingAgent_life")
    private double life;

    @JsonIgnore
    @ManyToOne(targetEntity = TrainingConfiguration.class, fetch = FetchType.LAZY)
    private TrainingConfiguration trainingConfiguration;

    @JsonIgnore
    public TrainingConfiguration getTrainingConfiguration() {
        return trainingConfiguration;
    }

    @JsonIgnore
    public void setTrainingConfiguration(TrainingConfiguration trainingConfiguration) {
        this.trainingConfiguration = trainingConfiguration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public WarAgentType getType() {
        return type;
    }

    public void setType(WarAgentType type) {
        this.type = type;
    }

    public double getLife() {
        return life;
    }

    public void setLife(double life) {
        this.life = life;
    }

    public void resetId() {
        setId(null);
    }
}
