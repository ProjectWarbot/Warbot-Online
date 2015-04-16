package edu.warbot.repository;

import edu.warbot.models.Account;
import edu.warbot.models.GameResult;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Date;

/**
 * Created by BEUGNON on 01/04/2015.
 *
 * @author Sébastien Beugnon
 */
public class GameResultRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private PasswordEncoder passwordEncoder;


    @Transactional
    public GameResult save(GameResult gameResult)
    {
        entityManager.persist(gameResult);
        return gameResult;
    }

    public GameResult findByLauncher(Party launcher) {
        try {
            return entityManager.createQuery
                    ("Select g From GameResult g Where g.launcher LIKE :launcher", GameResult.class)
                    .setParameter("launcher", launcher)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

public GameResult findByDateLauncherTarget(Party launcher,Party target, Date launchDate) {
        try {
            return entityManager.createQuery
                    ("Select g From GameResult g Where g.launcher LIKE :launcher And g.target LIKE :target And g.launchDate LIKE :launchDate", GameResult.class)
                    .setParameter("launcher", launcher).setParameter("target", target).setParameter("launchDate", launchDate)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }
}
