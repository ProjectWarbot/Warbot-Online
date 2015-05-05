package edu.warbot.services.impl;

import edu.warbot.game.Team;
import edu.warbot.game.WarGameSettings;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.online.WebGame;
import edu.warbot.online.WebLauncher;
import edu.warbot.online.WebGameSettings;
import edu.warbot.repository.PartyRepository;
import edu.warbot.services.TeamService;
import edu.warbot.services.WebGameService;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by beugnon on 05/04/15.
 *
 * @author beugnon
 */
@Service
public class WebGameServiceImpl
        implements WebGameService, ApplicationListener<BrokerAvailabilityEvent>
{
    private static Log logger = LogFactory.getLog(WebGameServiceImpl.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private TeamService teamService;

    private AtomicBoolean brokerAvailable = new AtomicBoolean();

    @Override
    public void onApplicationEvent(BrokerAvailabilityEvent event) {
        this.brokerAvailable.set(event.isBrokerAvailable());
    }

    public void startWebGame(Account account,WebGameSettings settings)
    {
        WarGameSettings wgs = new WarGameSettings();

        Party party1 = partyRepository.findOne(settings.getIdTeam1());
        Party party2 = partyRepository.findOne(settings.getIdTeam2());

        Team t1 = teamService.generateTeamFromParty(party1);
        Team t2 = teamService.generateTeamFromParty(party2);

        Assert.notNull(t1);
        Assert.notNull(t2);

        wgs.addSelectedTeam(t1);
        wgs.addSelectedTeam(t2);


        WebGame game = new WebGame(account.getEmail(),
                messagingTemplate,wgs);
        WebLauncher wl = new WebLauncher(game);

        new Madkit(Madkit.BooleanOption.desktop.toString(),"false").doAction(KernelAction.LAUNCH_AGENT,wl);
    }

    public void startAgainstIA(Account account,Party party)
    {
        WarGameSettings wgs = new WarGameSettings();
        Team t1 = teamService.generateTeamFromParty(party);
        Team t2  = teamService.getIATeamByName("Engineer");
        Assert.notNull(t1);
        wgs.addSelectedTeam(t1);
        wgs.addSelectedTeam(t2);
        WebGame game = new WebGame(account.getEmail(),
                messagingTemplate,wgs);
        WebLauncher wl = new WebLauncher(game);

        Madkit m = new Madkit(Madkit.BooleanOption.desktop.toString(),"false");
        m.doAction(KernelAction.LAUNCH_AGENT,wl);
    }

    public void startExampleWebGame(Account account)
    {
        logger.debug("trying running a example game");
        WarGameSettings wgs = new WarGameSettings();
        Team t1 = teamService.getIATeamByName("Engineer");

        Team t2 = t1.duplicate("duplicate");

        logger.info("team t1 -> "+t1.getName());
        logger.info("team t2 -> " + t2.getName());

        wgs.addSelectedTeam(t1);
        wgs.addSelectedTeam(t2);

        //TODO GIVE UUID TO GAME
        UUID uuid = UUID.randomUUID();// ID OF GAME

        WebGame game = new WebGame(account.getEmail(),
                messagingTemplate,wgs);
        //MUST SAVE WebGame


        WebLauncher wl = new WebLauncher(game);
        Madkit m = new Madkit(Madkit.BooleanOption.desktop.toString(),"false");
        m.doAction(KernelAction.LAUNCH_AGENT,wl);
    }

}
