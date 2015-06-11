package edu.warbot.online.services;

import edu.warbot.game.Team;
import edu.warbot.online.models.Party;

/**
 * Created by beugnon on 22/04/15.
 *
 * Service servant à générer des instances de Team Warbot à partir de scripts
 * ou de source java.
 *
 * @author beugnon
 *
 *
 */
public interface TeamService {
    /**
     * @param party l'équipe du site
     * @return une instance d'équipe Warbot avec les comportements scriptés par les utilisateurs
     */
    Team generateTeamFromParty(Party party);

    /**
     *
     * @param name le nom d'une intelligence artificielle déjà présente dans Warbot
     * @return une instance d'équipe Warbot
     */
    Team getIATeamByName(String name);

    /**
     *
     * @return une instance d'équipe de Warbot choisie aléatoirement
     */
    Team getRandomIATeam();
}
