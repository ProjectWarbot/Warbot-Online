package edu.warbot.services;

import edu.warbot.exceptions.AlreadyRunningGameException;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.process.communication.WebGameSettings;

/**
 * Created by beugnon on 22/04/15.
 */

public interface WebGameService {
    void startWebGame(Account account,WebGameSettings settings) throws AlreadyRunningGameException;
    void startAgainstIA(Account account,Party party) throws AlreadyRunningGameException;
    void startExampleWebGame(Account account) throws AlreadyRunningGameException;
}
