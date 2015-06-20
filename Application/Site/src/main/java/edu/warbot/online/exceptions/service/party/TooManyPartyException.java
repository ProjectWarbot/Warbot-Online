package edu.warbot.online.exceptions.service.party;

import edu.warbot.online.models.Account;

/**
 * Created by beugnon on 12/06/15.
 */
public class TooManyPartyException extends Exception {

    private Account account;

    public TooManyPartyException(Account account) {
        super();
        this.account = account;
    }

    public String getMessage() {
        return "The account '" +
                account.getEmail() +
                "' reached the limit of parties.";
    }
}
