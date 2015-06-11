package edu.warbot.online.services;

import edu.warbot.online.models.Account;
import edu.warbot.online.models.Party;
import edu.warbot.online.models.WebAgent;
import edu.warbot.online.models.WebCode;

import java.util.List;

/**
 * Created by beugnon on 08/04/15.
 *
 * @author beugnon
 */
public interface WarbotOnlineService {

    /**
     * Création d'une équipe
     *
     * @param party
     * @return
     */
    Party createParty(Party party);

    List<WebAgent> findAgentsForParty(Party party);

    List<WebCode> findWebCodesForParty(Party party);

    WebCode findWebCodeForPartyAndAgent(Party party, WebAgent agent);

    Party findPartyById(Long id);

    Party findPartyByName(String name);

    Iterable<Party> findAllParty();

    List<Party> findPartyByCreator(Account account);

    void deleteParty(Long id);

    void addMember(Party party, Account account);

    void removeMember(Party party, Account member);

}
