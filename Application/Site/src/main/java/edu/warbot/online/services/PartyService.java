package edu.warbot.online.services;

import edu.warbot.online.exceptions.NotFoundEntityException;
import edu.warbot.online.exceptions.service.party.AlreadyExistPartyException;
import edu.warbot.online.exceptions.service.party.AlreadyMemberException;
import edu.warbot.online.exceptions.service.party.TooManyMemberException;
import edu.warbot.online.exceptions.service.party.TooManyPartyException;
import edu.warbot.online.models.Account;
import edu.warbot.online.models.Party;

/**
 * Created by beugnon on 12/06/15.
 */
public interface PartyService {


    Party createParty(Account creator, Party party)
            throws TooManyPartyException, AlreadyExistPartyException;

    void deleteParty(Account creator, Party party)
            throws NotFoundEntityException;

    Party addMember(Account creator, Party party, Account member)
            throws AlreadyMemberException, TooManyMemberException;

    Party removeMember(Account creator, Party party, Account member)
            throws NotMemberException;

}
