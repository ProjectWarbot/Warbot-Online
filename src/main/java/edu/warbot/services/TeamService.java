package edu.warbot.services;

import edu.warbot.game.Team;
import edu.warbot.models.Party;

/**
 * Created by beugnon on 22/04/15.
 */
public interface TeamService {
    Team generateTeamFromParty(Party party);

    Team getIATeamByName(String name);

    Team getRandomIATeam();
}
