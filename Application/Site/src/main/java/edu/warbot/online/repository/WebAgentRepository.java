package edu.warbot.online.repository;


import com.javaetmoi.core.persistence.hibernate.LazyLoadingUtil;
import edu.warbot.online.models.Party;
import edu.warbot.online.models.WebAgent;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class WebAgentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public WebAgent save(WebAgent agent) {
        if (agent.getId() == null)
            entityManager.persist(agent);
        else
            entityManager.merge(agent);
        return agent;
    }

    public WebAgent findByType(String type) {
        try {
            WebAgent wa = entityManager.createQuery
                    ("Select a From WebAgent a Where a.type LIKE :type", WebAgent.class)
                    .setParameter("type", type)
                    .getSingleResult();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), wa);
            return wa;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public List<WebAgent> findAllPremiumAndActivated() {
        try {
            List<WebAgent> wa = entityManager.createQuery
                    ("Select a From WebAgent a Where isPremium = true And a.isActivated = true", WebAgent.class)
                    .getResultList();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), wa);
            return wa;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public List<WebAgent> findAllActivated() {
        try {
            List<WebAgent> wa = entityManager.createQuery
                    ("Select a From WebAgent a Where a.isActivated = true",
                            WebAgent.class)
                    .getResultList();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), wa);
            return wa;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public List<WebAgent> findAllPremium() {
        try {
            List<WebAgent> wa = entityManager.createQuery
                    ("Select a From WebAgent a Where isPremium = true", WebAgent.class)
                    .getResultList();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), wa);
            return wa;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public WebAgent findOne(Long aLong) {
        try {
            WebAgent wa = entityManager.createQuery
                    ("Select a From WebAgent a Where a.id = :id",
                            WebAgent.class)
                    .setParameter("id", aLong)
                    .getSingleResult();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), wa);
            return wa;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public List<WebAgent> findAll() {
        try {
            List<WebAgent> wa = entityManager.createQuery
                    ("Select a From WebAgent a", WebAgent.class)
                    .getResultList();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), wa);
            return wa;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public List<WebAgent> findAllStarter() {
        try {
            List<WebAgent> list = entityManager.createQuery
                    ("Select a From WebAgent a Where a.isActivated = true " +
                            "And a.isPremium = false", WebAgent.class)
                    .getResultList();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), list);
            return list;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public List<WebAgent> findForParty(Party party) {
        try {
            List<WebAgent> list = entityManager.createQuery
                    ("Select a From WebAgent a Where a.isActivated = true And " +
                            "a.isPremium = :premium", WebAgent.class)
                    .setParameter("premium", party.getCreator().isPremium())
                    .getResultList();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), list);
            return list;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }
}
