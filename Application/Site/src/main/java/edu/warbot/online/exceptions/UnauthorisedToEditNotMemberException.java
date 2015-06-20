package edu.warbot.online.exceptions;

import edu.warbot.online.models.Account;
import edu.warbot.online.models.Party;

/**
 * Created by BEUGNON on 01/04/2015.
 *
 * @author Sébastien Beugnon
 */
public class UnauthorisedToEditNotMemberException extends Exception {

    public UnauthorisedToEditNotMemberException(Account editor, Party party) {

    }
}
