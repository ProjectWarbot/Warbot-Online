package edu.warbot.online.repository;

import edu.warbot.online.models.Party;
import edu.warbot.online.models.WebAgent;
import edu.warbot.online.models.WebCode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

/**
 * Created by BEUGNON on 01/04/2015.
 *
 * @author Sebastien Beugnon
 */
@Repository
@Transactional
public class WebCodeRepository {

    /**
     *
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Méthode de persistance des données
     *
     * @param code l'instance de code à persister
     * @return l'instance de code persistée
     */

    public WebCode save(WebCode code) {
        if (findOne(code.getId()) == null)
            entityManager.persist(code);
        else
            entityManager.merge(code);
        return code;
    }


    public WebCode findOne(Long aLong) {
        try {
            return entityManager.createQuery
                    ("Select a From WebCode a Where a.id = :id", WebCode.class)
                    .setParameter("id", aLong)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    /**
     * Donne le code de tout les agents implémentées par une équipe
     *
     * @param team Une équipe de joueurs
     * @return la liste d'instances de code
     */
    public List<WebCode> findWebCodeForTeam(Party team) {
        try {
            return entityManager.createQuery
                    ("Select a From WebCode a Where party = :party", WebCode.class)
                    .setParameter("party", team)
                    .getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Donne le code d'une équipe pour un agent
     *
     * @param team  Une équipe de joueurs
     * @param agent Un type d'agent autorisé par l'interface Web
     * @return l'instance de code pour l'équipe et l'agent
     */
    public WebCode findWebCodeForTeamAndWebAgent(Party team, WebAgent agent) {
        try {
            return entityManager.createQuery
                    ("Select a From WebCode a Where party = :party" +
                            " And agent = :agent", WebCode.class)
                    .setParameter("party", team)
                    .setParameter("agent", agent)
                    .getSingleResult();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public boolean deleteCodeForParty(Long partyId) {
        try {
            entityManager.createQuery
                    ("DELETE From WebCode a Where" + " a.party.id = :partyId")
                    .setParameter("partyId", partyId).executeUpdate();
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }
}
