package edu.warbot.online.exceptions;

import edu.warbot.online.models.Account;
import edu.warbot.online.models.WebAgent;

/**
 * Created by BEUGNON on 01/04/2015.
 *
 * @author Sébastien Beugnon
 */
public class UnauthorisedToEditLockException extends Exception {

    private Account account;

    private WebAgent agent;

    public UnauthorisedToEditLockException(Account editor, WebAgent agent) {

    }
}
