package edu.warbot.services;

import edu.warbot.exceptions.UnauthorisedToEditLockException;
import edu.warbot.exceptions.UnauthorisedToEditNotMemberException;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;

/**
 * Created by beugnon on 22/04/15.
 */
public interface CodeEditorService {

    void saveWebCode(Account editor, WebCode webCode)
            throws UnauthorisedToEditLockException,
            UnauthorisedToEditNotMemberException;

    WebCode getWebCode(Party party, WebAgent agent);

}
