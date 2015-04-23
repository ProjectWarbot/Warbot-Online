package edu.warbot.models;

import edu.warbot.models.enums.LevelTrainingConfigurationEnum;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by quent on 23/04/2015.
 */
@Entity
@Table(name = "MAP")
@NamedQuery(name = Map.FIND_BY_NAME,query = "select m from Map m where m.name = :name")
public class Map extends AbstractPersistable<Long> {

    public static final String FIND_BY_NAME = "Map.findByName";


    @Column(name = "map_name", unique = true)
    private String name;

    @Column(name = "map_desc", unique = false)
    private String desc;

    @Column(name = "map_difficulty", unique = false)
    private LevelTrainingConfigurationEnum difficulty;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Account.class)
    private Account creator;

    @ManyToMany(targetEntity = TrainingAgent.class, fetch = FetchType.LAZY, mappedBy = "teams")
    private Set<TrainingAgent> trainingAgents = new HashSet<>();
}
