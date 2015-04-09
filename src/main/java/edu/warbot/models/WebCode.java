package edu.warbot.models;

import org.hibernate.metamodel.source.annotations.attribute.type.LobTypeResolver;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by BEUGNON on 30/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Entity
@Table(name = "CODE")
public class WebCode extends AbstractPersistable<Long>
{
    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    private Party party;

    @ManyToOne(targetEntity = WebAgent.class)
    private WebAgent agent;


    @Lob
    @Column(name = "code_content", length=64000)
    private String content;

    @Column(name = "code_lastModification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModification;


    public WebCode()
    {

    }


    public WebCode(WebAgent agent, Party party) {
        this();
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

    public String getContent() {
        return content;
    }

    public void setContent(String text) {
        this.content = text;
    }

    public Date getLastModification() {
        return lastModification;
    }

    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

}
