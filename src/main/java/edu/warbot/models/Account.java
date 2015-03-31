package edu.warbot.models;

import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACCOUNT")
@NamedQueries(
        {
                @NamedQuery(name = Account.FIND_BY_EMAIL,
                        query = "select a from Account a where a.email = :email")
                ,
                @NamedQuery(name = Account.FIND_BY_SCREEN_NAME,
                        query = "select a from Account a where a.screenName = :screenName")
        }
)
public class Account extends AbstractPersistable<Long> {

    public static final String FIND_BY_EMAIL = "Account.findByEmail";
    public static final String FIND_BY_SCREEN_NAME = "Account.findByScreenName";


    @Column(name = "account_email",unique = true,nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "account_password",nullable = false)
    private String password;

    @Column(name = "account_firstname",nullable = false)
    private String firstName;

    @Column(name = "account_lastname",nullable = false)
    private String lastName;

    @Column(name = "account_screenname",nullable = false,unique = true)
    private String screenName;

    @Column(name = "account_activated",nullable = false)
    private boolean isActivated;

    @Column(name = "account_premium",nullable = false)
    private boolean isPremium;

    @Column(name = "account_inscriptionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date inscriptionDate;

    @Column(name = "account_expirationDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date premiumExpirationDate;

    @Column(name = "account_lastConnectionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastConnectionDate;

    @Column(name = "account_role")
    private String role = "ROLE_USER";

    @ManyToMany(targetEntity = Party.class, cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinTable(name = "PARTY_MEMBERS",
            joinColumns =
                    {
                            @JoinColumn(name ="account_id",nullable = false,updatable = true)},
            inverseJoinColumns = {
                    @JoinColumn(name="party_id",nullable = false,updatable = true)
            })
    private Set<Party> teams= new HashSet<>();

    @OneToMany (mappedBy="creator", cascade={CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Party> created = new HashSet<>();

    protected Account() {

    }

    public Account(String email, String password, String firstName, String lastName, String screenName, boolean isActivated, boolean isPremium, Date inscriptionDate, Date premiumExpirationDate, Date lastConnectionDate, String role, Set<Party> teams) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.screenName = screenName;
        this.isActivated = isActivated;
        this.isPremium = isPremium;
        this.inscriptionDate = inscriptionDate;
        this.premiumExpirationDate = premiumExpirationDate;
        this.lastConnectionDate = lastConnectionDate;
        this.role = role;
        this.teams = teams;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Party> getTeams() {
        return teams;
    }

    public void setTeams(Set<Party> teams) {
        this.teams = teams;
    }

    public Date getLastConnectionDate() {
        return lastConnectionDate;
    }

    public void setLastConnectionDate(Date lastConnectionDate) {
        this.lastConnectionDate = lastConnectionDate;
    }

    public Date getPremiumExpirationDate() {
        return premiumExpirationDate;
    }

    public void setPremiumExpirationDate(Date premiumExpirationDate) {
        this.premiumExpirationDate = premiumExpirationDate;
    }

    public Date getInscriptionDate() {
        return inscriptionDate;
    }

    public void setInscriptionDate(Date inscriptionDate) {
        this.inscriptionDate = inscriptionDate;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivate) {
        this.isActivated = isActivate;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
