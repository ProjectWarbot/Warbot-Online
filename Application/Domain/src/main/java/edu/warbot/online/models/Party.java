package edu.warbot.online.models;

import edu.warbot.scriptcore.interpreter.ScriptInterpreterLanguage;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PARTY")
@NamedQuery(name = Party.FIND_BY_NAME, query = "select p from Party p where p.name = :name")
public class Party extends AbstractPersistable<Long> {

    public static final String FIND_BY_NAME = "Party.findByName";


    @Column(name = "party_name", unique = true)
    private String name;

    @Column(name = "party_language")
    private ScriptInterpreterLanguage language;

    @Column(name = "party_elo")
    private int eloRank;

    @Column(name = "party_creationDate")
    private Date creationDate;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
    private Account creator;

    @ManyToMany(targetEntity = Account.class,
            fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private Set<Account> members = new HashSet<>();

    @OneToMany(targetEntity = WebCode.class, mappedBy = "party")
    private Set<WebCode> agents = new HashSet<>();

    protected Party() {
        this.members = new HashSet<>();
    }

    public Party(String name, ScriptInterpreterLanguage language) {
        this();
        this.name = name;
        this.language = language;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Account> getMembers() {
        return this.members;
    }

    public void setMembers(Set<Account> members) {
        this.members = members;
    }

    public void addMember(Account member) {
        this.members.add(member);
    }

    public ScriptInterpreterLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ScriptInterpreterLanguage language) {
        this.language = language;
    }

    public int getEloRank() {
        return eloRank;
    }

    public void setEloRank(int eloRank) {
        this.eloRank = eloRank;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Account getCreator() {
        return creator;
    }

    public void setCreator(Account creator) {
        this.creator = creator;
    }

    public Set<WebCode> getAgents() {
        return agents;
    }

    public void setAgents(Set<WebCode> agents) {
        this.agents = agents;
    }
}
