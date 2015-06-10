package edu.warbot.online.services;

import edu.warbot.online.exceptions.UnauthorisedToEditLockException;
import edu.warbot.online.exceptions.UnauthorisedToEditNotMemberException;
import edu.warbot.online.models.Account;
import edu.warbot.online.models.Party;
import edu.warbot.online.models.WebAgent;
import edu.warbot.online.models.WebCode;

/**
 * Created by beugnon on 22/04/15.
 */
public interface CodeEditorService {

    void saveWebCode(Account editor, WebCode webCode)
            throws UnauthorisedToEditLockException,
            UnauthorisedToEditNotMemberException;

    WebCode getWebCode(Party party, WebAgent agent);

    boolean deleteCodeForParty(Long partyId);

}
