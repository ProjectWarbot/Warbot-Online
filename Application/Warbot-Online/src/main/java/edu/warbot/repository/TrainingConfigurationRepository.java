package edu.warbot.repository;


import com.javaetmoi.core.persistence.hibernate.LazyLoadingUtil;
import edu.warbot.models.TrainingConfiguration;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by quent on 23/04/2015.
 */
@Repository
@Transactional

public class TrainingConfigurationRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public TrainingConfiguration findByName(String name) {
        try {
            return entityManager.createNamedQuery(TrainingConfiguration.FIND_BY_NAME, TrainingConfiguration.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public <S extends TrainingConfiguration> S save(S s) {
        if (s.getId() == null)
            entityManager.persist(s);
        else
            entityManager.merge(s);
        return s;
    }

    public TrainingConfiguration findOne(Long aLong) {
        try {
            TrainingConfiguration tc = entityManager.createQuery("Select m from TrainingConfiguration m where m.id = :id", TrainingConfiguration.class)
                    .setParameter("id", aLong)
                    .getSingleResult();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), tc);
            return tc;
        } catch (PersistenceException e) {
            return null;
        }
    }

    //    @Override
    public boolean exists(Long aLong) {
        return findOne(aLong) != null;
    }

    //    @Override
    public Iterable<TrainingConfiguration> findAll() {
        List<TrainingConfiguration> list = entityManager.createQuery("Select m from TrainingConfiguration m ", TrainingConfiguration.class).getResultList();
        LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class), list);
        return list;
    }

    //    @Override
    public long count() {
        return (Long) entityManager.createQuery("Select Count * FROM TrainingConfiguration").getSingleResult();
    }

    //    @Override
    public void delete(TrainingConfiguration map) {
        entityManager.createQuery("Delete FROM TrainingConfiguration m WHERE m.id = :id")
                .setParameter("id", map.getId()).executeUpdate();
    }

}
