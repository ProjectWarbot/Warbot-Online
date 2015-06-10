package edu.warbot.models;

import edu.warbot.models.enums.GameResultEnum;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by BEUGNON on 01/04/2015.
 *
 * @author Sébastien Beugnon
 */
@Entity
@Table(name = "GAME_RESULT")
public class GameResult extends AbstractPersistable<Long> {
    @ManyToOne(targetEntity = Party.class)
    private Party launcher;

    @ManyToOne(targetEntity = Party.class)
    private Party target;

    @Column(name = "result")
    private GameResultEnum result;

    @Column(name = "launchDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date launchDate;


    public GameResult() {

    }

    public GameResult(Party team1, Party team2, GameResultEnum result, Date launchDate) {
        this.launcher = team1;
        this.target = team2;
        this.result = result;
        this.launchDate = launchDate;
    }

    public Party getLauncher() {
        return launcher;
    }

    public void setLauncher(Party launcher) {
        this.launcher = launcher;
    }

    public Party getTarget() {
        return target;
    }

    public void setTarget(Party target) {
        this.target = target;
    }

    public GameResultEnum getResult() {
        return result;
    }

    public void setResult(GameResultEnum result) {
        this.result = result;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }
}
