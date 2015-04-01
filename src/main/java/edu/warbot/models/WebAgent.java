package edu.warbot.models;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by BEUGNON on 30/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Entity
@Table(name = "WEB_AGENT")
public class WebAgent extends AbstractPersistable<Long>
{

    @Column(name = "WarType")
    private WarAgentType type;

    @Column(name = "isActivated")
    private boolean isActivated;

    @Column(name = "isPremium")
    private boolean isPremium;

    public WebAgent(WarAgentType type, boolean isActivated, boolean isPremium) {
        this.type = type;
        this.isActivated = isActivated;
        this.isPremium = isPremium;
    }

    public WarAgentType getType() {
        return type;
    }

    public void setType(WarAgentType type) {
        this.type = type;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setIsPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }
}
