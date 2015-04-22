package edu.warbot.controllers;

import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.online.messaging.WebGameSettings;
import edu.warbot.repository.AccountRepository;
import edu.warbot.services.WarbotOnlineService;
import edu.warbot.services.WebGameServiceImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.security.Principal;

@Controller
@Secured({"ROLE_USER","ROLE_ADMIN"})
public class WebGameController {


    private static final Log logger = LogFactory.getLog(WebGameController.class);

    @Autowired
    private WebGameServiceImpl webGameService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WarbotOnlineService warbotOnlineService;


    @SubscribeMapping("/game/register")
    public void registerUser(Principal principal)
    {
        Assert.notNull(principal);

    }


    @MessageMapping("/game/start")
    public void startGame(Principal principal, WebGameSettings settings)
    {
        Assert.notNull(principal);
        logger.debug(settings);
        Account account = accountRepository.findByEmail(principal.getName());
        webGameService.startExampleWebGame(account);

        //TODO RETURN RESULT OF WebGameSettings
    }

    @MessageMapping("/game/start.against.ia")
    public void startGameAgainstIA(Principal principal,
                          WebGameSettings settings) throws Exception {
        Assert.notNull(principal);
        logger.debug(settings);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = warbotOnlineService.findPartyById(settings.getIdTeam1());
        Assert.notNull(party);
        //if(party.getMembers().contains(account) || party.getCreator().equals(account))
        webGameService.startAgainstIA(account, party);
        //else
    }
}
