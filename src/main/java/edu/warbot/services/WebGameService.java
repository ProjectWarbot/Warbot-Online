package edu.warbot.services;

import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.online.WebGameSettings;

/**
 * Created by beugnon on 22/04/15.
 */

public interface WebGameService {
    void startWebGame(Account account,WebGameSettings settings);
    void startAgainstIA(Account account,Party party);
    void startExampleWebGame(Account account);
}
