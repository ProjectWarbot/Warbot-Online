package edu.warbot.online.controllers;

import edu.warbot.online.exceptions.AlreadyRunningGameException;
import edu.warbot.online.models.Account;
import edu.warbot.online.models.Party;
import edu.warbot.online.process.communication.WebGameSettings;
import edu.warbot.online.repository.AccountRepository;
import edu.warbot.online.services.WarbotOnlineService;
import edu.warbot.online.services.WebGameService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.security.Principal;

@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class WebGameController {


    private static final Logger logger = LoggerFactory.getLogger(WebGameController.class);

    @Autowired
    private WebGameService webGameService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WarbotOnlineService warbotOnlineService;


    @SubscribeMapping("/game/register")
    public void registerUser(Principal principal) {
        Assert.notNull(principal);

    }


    @MessageMapping("/game/start")
    public void startGame(Principal principal, WebGameSettings settings) throws AlreadyRunningGameException {
        Assert.notNull(principal);
        logger.debug(settings.toString());
        Account account = accountRepository.findByEmail(principal.getName());
        webGameService.startExampleWebGame(account);
    }

    @MessageMapping("/game/start.duel")
    public void startWebGame(Principal principal, WebGameSettings settings) throws AlreadyRunningGameException {
        Assert.notNull(principal);
        logger.info(settings.toString());
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = warbotOnlineService.findPartyById(settings.getIdTeam1());
        Assert.notNull(party);

        Party party2;
        party2 = warbotOnlineService.findPartyById(settings.getIdTeam2());
        Assert.notNull(party2);

        if (party.getMembers().contains(account) || party.getCreator().equals(account))
            webGameService.startWebGame(account, settings);
    }

    @MessageMapping("/game/start.against.ia")
    public void startGameAgainstIA(Principal principal,
                                   WebGameSettings settings) throws Exception, AlreadyRunningGameException {
        Assert.notNull(principal);
        logger.debug(settings.toString());
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = warbotOnlineService.findPartyById(settings.getIdTeam1());
        Assert.notNull(party);
        if (party.getMembers().contains(account) || party.getCreator().equals(account))
            webGameService.startAgainstIA(account, party);
        //else
    }

    @MessageMapping("/game/stop")
    public void stopGame(Principal principal) {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        webGameService.stopGame(account);
    }

    @MessageMapping("/game/pause")
    public void pauseGame(Principal principal) {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        webGameService.pauseGame(account);
    }


}
