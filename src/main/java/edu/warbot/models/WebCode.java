package edu.warbot.models;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by BEUGNON on 30/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Entity
@Table(name = "CODE")
public class WebCode extends AbstractPersistable<Long>
{
    @ManyToOne(targetEntity = Party.class)
    private Party party;

    @ManyToOne(targetEntity = WebAgent.class)
    private WebAgent agent;


    public WebCode(WebAgent agent, Party party) {
        this.agent = agent;
        this.party = party;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public WebAgent getAgent() {
        return agent;
    }

    public void setAgent(WebAgent agent) {
        this.agent = agent;
    }
}
