package edu.warbot.services;

import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;

import java.util.List;

/**
 * Created by beugnon on 08/04/15.
 */
public interface WarbotOnlineService
{
    Party createParty(Party party);

    List<WebAgent> findAgentsForParty(Party party);

    List<WebCode> findWebCodesForParty(Party party);

    WebCode findWebCodeForPartyAndAgent(Party party, WebAgent agent);

    Party findPartyById(Long id);

    Party findPartyByName(String name);

    Iterable<Party> findAllParty();

    List<Party> findPartyByCreator(Account account);

    boolean deleteParty(Long id);

    void addMember(Party party,Account account);
}
