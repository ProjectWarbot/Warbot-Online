package edu.warbot.models;

import edu.warbot.models.enums.LevelTrainingConfigurationEnum;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * Created by jimmy on 14/04/15.
 */

@Entity
@Table(name = "TRAINING_CONFIGURATION")
@NamedQuery(name = TrainingConfiguration.FIND_BY_NAME,query = "select z from TrainingConfiguration z where z.name = :name")
public class TrainingConfiguration extends AbstractPersistable<Long> {

    public static final String FIND_BY_NAME = "TrainingConfiguration.findByName";

    @Column(name="training_configuration_name",unique = true)
    private String name;

    @Column(name="training_configuration_description")
    private String description;

    @Column(name="training_configuration_level")
    private LevelTrainingConfigurationEnum level;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Account.class)
    private Account creator;

    public TrainingConfiguration() {

    }

    public TrainingConfiguration(String name, LevelTrainingConfigurationEnum level, String description) {
        this.name = name;
        this.description = description;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LevelTrainingConfigurationEnum getLevel() {
        return level;
    }

    public Account getCreator() {
        return creator;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLevel(LevelTrainingConfigurationEnum level) {
        this.level = level;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }
}
