package edu.warbot.online.services;

import edu.warbot.game.Team;
import edu.warbot.online.models.Party;

/**
 * Created by beugnon on 22/04/15.
 */
public interface TeamService {
    Team generateTeamFromParty(Party party);

    Team getIATeamByName(String name);

    Team getRandomIATeam();
}
