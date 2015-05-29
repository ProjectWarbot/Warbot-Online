package edu.warbot.models;

import edu.warbot.models.enums.LevelTrainingConfigurationEnum;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jimmy on 14/04/15.
 */

@Entity
@Table(name = "TRAINING_CONFIGURATION")
@NamedQuery(name = TrainingConfiguration.FIND_BY_NAME, query = "select z from TrainingConfiguration z where z.name = :name")
public class TrainingConfiguration extends AbstractPersistable<Long> {

    public static final String FIND_BY_NAME = "TrainingConfiguration.findByName";

    @Column(name = "training_configuration_name", unique = true)
    private String name;

    @Column(name = "training_configuration_description")
    private String description;

    @Column(name = "training_configuration_level")
    private LevelTrainingConfigurationEnum level;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    private Account creator;
    @ManyToMany(targetEntity = TrainingAgent.class, fetch = FetchType.LAZY)
    private Set<TrainingAgent> trainingAgents = new HashSet<>();

    public TrainingConfiguration() {

    }

    public TrainingConfiguration(String name, LevelTrainingConfigurationEnum level, String description) {
        this.name = name;
        this.description = description;
        this.level = level;
    }

    public Set<TrainingAgent> getTrainingAgents() {
        return trainingAgents;
    }

    public void setTrainingAgents(Set<TrainingAgent> trainingAgents) {
        this.trainingAgents = trainingAgents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LevelTrainingConfigurationEnum getLevel() {
        return level;
    }

    public void setLevel(LevelTrainingConfigurationEnum level) {
        this.level = level;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }
}
