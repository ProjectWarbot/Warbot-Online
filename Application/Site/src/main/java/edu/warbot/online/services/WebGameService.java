package edu.warbot.online.services;

import edu.warbot.online.exceptions.AlreadyRunningGameException;
import edu.warbot.online.models.Account;
import edu.warbot.online.models.Party;
import edu.warbot.online.process.communication.WebGameSettings;

/**
 * Created by beugnon on 22/04/15.
 */

public interface WebGameService {
    void startWebGame(Account account, WebGameSettings settings) throws AlreadyRunningGameException;

    void startAgainstIA(Account account, Party party) throws AlreadyRunningGameException;

    void startExampleWebGame(Account account) throws AlreadyRunningGameException;

    void stopGame(Account account);

    void pauseGame(Account account);

    void preciseAgentFromGame(Account account, String id);
}
