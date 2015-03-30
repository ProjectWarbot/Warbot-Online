package edu.warbot.party;

import javax.persistence.*;

import edu.warbot.account.Account;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PARTY")
@NamedQuery(name = Party.FIND_BY_NAME, query = "select p from Party p where p.name = :name")
public class Party extends AbstractPersistable<Long>
{

    public static final String FIND_BY_NAME = "Party.findByName";


    @Column(name="party_name",unique = true)
    private String name;

    @Column(name="party_language")
    private String language;

    @Column(name =  "party_elo")
    private int eloRank;

    @Column(name = "party_creationDate")
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY,targetEntity = Account.class)
    private Account creator;

    @ManyToMany(targetEntity = Account.class,
            fetch = FetchType.LAZY,
            mappedBy = "teams")
    private Set<Account> members=new HashSet<>();

    protected Party() { }

    public Party(String name,String language) {
        this.name = name;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public Set<Account> getMembers()
    {
        return this.members;
    }

    public void setMembers(Set<Account> members)
    {
        this.members = members;
    }
}
