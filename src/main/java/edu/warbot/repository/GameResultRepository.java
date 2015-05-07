package edu.warbot.repository;

import com.javaetmoi.core.persistence.hibernate.LazyLoadingUtil;
import edu.warbot.models.Account;
import edu.warbot.models.GameResult;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import org.hibernate.Session;
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
        if(gameResult.getId()==null)
            entityManager.persist(gameResult);
        else
            entityManager.merge(gameResult);
        LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class),gameResult);
        return gameResult;
    }

    public GameResult findByLauncher(Party launcher) {
        try {
            GameResult gr = entityManager.createQuery
                    ("Select g From GameResult g Where g.launcher LIKE :launcher", GameResult.class)
                    .setParameter("launcher", launcher)
                    .getSingleResult();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class),gr);
            return gr;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public GameResult findByDateLauncherTarget(Party launcher,Party target, Date launchDate) {
        try {
            GameResult gr = entityManager.createQuery
                    ("Select g From GameResult g Where g.launcher LIKE :launcher And g.target LIKE :target And g.launchDate LIKE :launchDate", GameResult.class)
                    .setParameter("launcher", launcher).setParameter("target", target).setParameter("launchDate", launchDate)
                    .getSingleResult();
            LazyLoadingUtil.deepHydrate(entityManager.unwrap(Session.class),gr);
            return gr;
        } catch (PersistenceException e) {
            return null;
        }
    }
}
