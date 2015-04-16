package edu.warbot.models;

import edu.warbot.models.enums.LevelTestZoneEnum;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 * Created by jimmy on 14/04/15.
 */

@Entity
@Table(name = "TEST_ZONE")
@NamedQuery(name = TestZone.FIND_BY_NAME,query = "select z from TestZone z where z.name = :name")
public class TestZone extends AbstractPersistable<Long> {

    public static final String FIND_BY_NAME = "TestZone.findByName";

    @Column(name="test_zone_name",unique = true)
    private String name;

    @Column(name="test_zone_description")
    private String description;

    @Column(name="test_zone_level")
    private LevelTestZoneEnum level;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Account.class)
    private Account creator;

    public TestZone() {

    }

    public TestZone(String name, LevelTestZoneEnum level, String description ) {
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

    public LevelTestZoneEnum getLevel() {
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

    public void setLevel(LevelTestZoneEnum level) {
        this.level = level;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }
}
