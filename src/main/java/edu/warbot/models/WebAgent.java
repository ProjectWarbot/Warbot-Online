package edu.warbot.models;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import org.springframework.data.jpa.domain.AbstractPersistable;

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

    private WarAgentType type;
    public WebAgent()
    {

    }
}
