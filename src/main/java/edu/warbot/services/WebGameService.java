package edu.warbot.services;

import edu.warbot.game.Team;
import edu.warbot.game.WarGameSettings;
import edu.warbot.launcher.WarLauncher;
import edu.warbot.launcher.WarMain;
import edu.warbot.launcher.WarScheduler;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.online.WebGame;
import edu.warbot.online.WebLauncher;
import edu.warbot.online.messaging.WebGameSettings;
import edu.warbot.repository.PartyRepository;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Service;
import turtlekit.kernel.TurtleKit;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by beugnon on 05/04/15.
 *
 * @author beugnon
 */
@Service
public class WebGameService
        implements ApplicationListener<BrokerAvailabilityEvent>
{
    private static Log logger = LogFactory.getLog(WebGameService.class);

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


    public void registeNewUser(Account a)
    {

    }

    public void startWebGame(Account account,WebGameSettings settings)
    {

        WarGameSettings wgs = new WarGameSettings();


        Party party1 = partyRepository.findOne(settings.getIdTeam1());

        Party party2 = partyRepository.findOne(settings.getIdTeam2());


        Team t1 = teamService.generateTeamFromParty(party1);
        Team t2 = teamService.generateTeamFromParty(party2);


        WebGame game = new WebGame(account.getEmail(),
                messagingTemplate,wgs);
        WebLauncher wl = new WebLauncher(game);

        new Madkit().doAction(KernelAction.LAUNCH_AGENT,wl);
    }

    public void startExampleWebGame(Account account)
    {
        logger.debug("trying running a example game");
        WarGameSettings wgs = new WarGameSettings();
        Collection<Team> coll = teamService.getTeams().values();

        Iterator<Team> it = coll.iterator();

        logger.info("have "+coll.size()+" default teams");
        Team t1 = it.next();
        Team t2;
        if(!it.hasNext())
            t2 = t1.duplicate("duplicate");
        else
            t2 = it.next();

        logger.info("team t1 -> "+t1.getName());
        logger.info("team t2 -> "+t2.getName());

        wgs.addSelectedTeam(t1);
        wgs.addSelectedTeam(t2);

        WebGame game = new WebGame(account.getEmail(),
                messagingTemplate,wgs);
        WebLauncher wl = new WebLauncher(game);

        //TurtleKit tk = new TurtleKit();
        //TODO SET AT FALSE TO HIDE DESKTOP
        Madkit m = new Madkit(Madkit.BooleanOption.desktop.toString(),"false");
        m.doAction(KernelAction.LAUNCH_AGENT, wl);
    }
}
