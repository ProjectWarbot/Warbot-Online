package edu.warbot.online.repository;

import edu.warbot.online.models.TrainingAgent;
import edu.warbot.online.models.TrainingConfiguration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

/**
 * Created by beugnon on 31/05/15.
 */
@Repository
@Transactional
public class TrainingAgentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public <S extends TrainingAgent> S save(S s) {
        if (s.getId() == null)
            entityManager.persist(s);
        else
            entityManager.merge(s);
        return s;
    }

    public List<TrainingAgent> findByAssociatedTrainingConfiguration
            (TrainingConfiguration tc) {
        try {
            List<TrainingAgent> wa = entityManager.createQuery
                    ("Select ta From TrainingAgent ta Where ta.trainingConfiguration = :tc",
                            TrainingAgent.class)
                    .setParameter("tc", tc)
                    .getResultList();
            return wa;
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    public void deleteByAssociatedTrainingConfiguration
            (TrainingConfiguration tc) {
        entityManager.createQuery("Delete FROM TrainingAgent ta WHERE ta.trainingConfiguration = :tc")
                .setParameter("tc", tc).executeUpdate();
    }

    public void delete(TrainingAgent trainingAgent) {
        entityManager.createQuery("Delete FROM TrainingAgent ta WHERE ta.id = :id")
                .setParameter("id", trainingAgent.getId()).executeUpdate();
    }

    public void deleteAll(Iterable<TrainingAgent> trainingAgents) {
        for (TrainingAgent t : trainingAgents)
            delete(t);
    }
}
