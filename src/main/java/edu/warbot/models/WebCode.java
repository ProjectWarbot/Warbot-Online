package edu.warbot.models;

import org.springframework.data.jpa.domain.AbstractPersistable;

<<<<<<< HEAD
import javax.persistence.*;
import java.util.Date;
=======
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa

/**
 * Created by BEUGNON on 30/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Entity
@Table(name = "CODE")
public class WebCode extends AbstractPersistable<Long>
{
<<<<<<< HEAD
    @ManyToOne(targetEntity = Party.class, fetch = FetchType.LAZY)
    private Party party;

    @ManyToOne(targetEntity = WebAgent.class,fetch = FetchType.LAZY)
    private WebAgent agent;

    @Column(name = "code_content")
    private String content;

    @Column(name = "code_lastModification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModification;

    public WebCode() {

    }
=======
    @ManyToOne(targetEntity = Party.class)
    private Party party;

    @ManyToOne(targetEntity = WebAgent.class)
    private WebAgent agent;


>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
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
<<<<<<< HEAD

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
=======
>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
}
