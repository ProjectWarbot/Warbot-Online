package edu.warbot.repository;

<<<<<<< HEAD
import edu.warbot.models.Party;
=======
import edu.warbot.agents.enums.WarAgentType;
>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
import edu.warbot.models.WebAgent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
=======
import javax.annotation.PostConstruct;
>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class WebAgentRepository
{
    @PersistenceContext
    private EntityManager entityManager;

<<<<<<< HEAD
=======

>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
    @Transactional
    public WebAgent save(WebAgent agent)
    {
        entityManager.persist(agent);
        return agent;
    }

    public WebAgent findByType(String type) {
        try
        {
            return entityManager.createQuery
<<<<<<< HEAD
                    ("Select a From WebAgent a Where a.type LIKE :type", WebAgent.class)
=======
                    ("Select a From WebAgent a Where type LIKE :type", WebAgent.class)
>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
                    .setParameter("type", type)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

<<<<<<< HEAD
    public List<WebAgent> findAllPremiumAndActivated() {
        try
        {
            return entityManager.createQuery
                    ("Select a From WebAgent a Where isPremium = true And a.isActivated = true", WebAgent.class)
                    .getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public List<WebAgent> findAllActivated() {
        try
        {
            return entityManager.createQuery
                    ("Select a From WebAgent a Where a.isActivated = true", WebAgent.class)
=======
    public List<WebAgent> findAllPremium() {
        try
        {
            return entityManager.createQuery
                    ("Select a From WebAgent a Where isPremium = true", WebAgent.class)
>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
                    .getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public WebAgent findOne(Long aLong) {
        try
        {
            return entityManager.createQuery
<<<<<<< HEAD
                    ("Select a From WebAgent a Where a.id = :id", WebAgent.class)
                    .setParameter("id", aLong)
=======
                    ("Select a From WebAgent a Where id = :id", WebAgent.class)
                    .setParameter("id",aLong)
>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public List<WebAgent> findAll() {
        try
        {
            return entityManager.createQuery
                    ("Select a From WebAgent a", WebAgent.class)
                    .getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }
<<<<<<< HEAD

    public List<WebAgent> findAllStarter()
    {
        try
        {
            return entityManager.createQuery
                    ("Select a From WebAgent a Where a.isActivated = true " +
                            "And a.isPremium = false", WebAgent.class)
                    .getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public List<WebAgent> findForParty(Party party)
    {
        try
        {
            return entityManager.createQuery
                    ("Select a From WebAgent a Where a.isActivated = true And " +
                            "a.isPremium = :premium", WebAgent.class)
                    .setParameter("premium",party.getCreator().isPremium())
                    .getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }
=======
>>>>>>> 22fcd8a86dcc9d7fa409ba8af58d4278535adbaa
}
