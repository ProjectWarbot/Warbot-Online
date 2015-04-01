package edu.warbot.repository;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.models.WebAgent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
                    ("Select a From WebAgent a Where type LIKE :type", WebAgent.class)
                    .setParameter("type", type)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public List<WebAgent> findAllPremium() {
        try
        {
            return entityManager.createQuery
                    ("Select a From WebAgent a Where isPremium = true", WebAgent.class)
                    .getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public WebAgent findOne(Long aLong) {
        try
        {
            return entityManager.createQuery
                    ("Select a From WebAgent a Where id = :id", WebAgent.class)
                    .setParameter("id",aLong)
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
}
