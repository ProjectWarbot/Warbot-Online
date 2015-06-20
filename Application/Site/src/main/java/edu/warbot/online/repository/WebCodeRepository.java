package edu.warbot.online.repository;

import edu.warbot.online.models.Party;
import edu.warbot.online.models.WebAgent;
import edu.warbot.online.models.WebCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by BEUGNON on 01/04/2015.
 *
 * @author Sebastien Beugnon
 */
public interface WebCodeRepository extends JpaRepository<WebCode, Long> {

    /**
     * Donne le code de tout les agents implémentées par une équipe
     *
     * @param party Une équipe de joueurs
     * @return la liste d'instances de code
     */
    List<WebCode> findWebCodeByParty(Party party);

    /**
     * Donne le code d'une équipe pour un agent
     *
     * @param party  Une équipe de joueurs
     * @param agent Un type d'agent autorisé par l'interface Web
     * @return l'instance de code pour l'équipe et l'agent
     */
    WebCode findWebCodeByPartyAndAgent(Party party, WebAgent agent);

    /**
     * Retire le code des agents pour une équipe
     *
     * @param party Une équipe de joueurs
     */
    void deleteByParty(Party party);

}
