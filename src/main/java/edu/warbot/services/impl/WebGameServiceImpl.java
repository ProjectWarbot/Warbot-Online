package edu.warbot.services.impl;

import edu.warbot.Application;
import edu.warbot.exceptions.AlreadyRunningGameException;
import edu.warbot.game.WarGame;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.process.communication.JSONInterProcessMessageTranslater;
import edu.warbot.process.communication.WebGameSettings;
import edu.warbot.process.communication.client.EndMessage;
import edu.warbot.process.communication.server.LaunchGameCommand;
import edu.warbot.process.communication.server.PreciseAgentCommand;
import edu.warbot.process.communication.server.RemoteWarbotCommand;
import edu.warbot.process.game.MainWarbot;
import edu.warbot.process.game.ServerWarbotGameAgent;
import edu.warbot.repository.AccountRepository;
import edu.warbot.services.WebGameService;
import edu.warbot.support.process.JVMBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import turtlekit.kernel.TurtleKit;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beugnon on 05/05/15.
 * <p/>
 * Classe lançant des processus de parties remplaçant le WebGameService
 */
@Service
public class WebGameServiceImpl implements WebGameService, ApplicationListener<SessionDisconnectEvent> {


    private Map<Account, ServerWarbotGameAgent> parties = new HashMap<>();

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SimpMessageSendingOperations templateMessaging;


    @Override
    public void onApplicationEvent(SessionDisconnectEvent applicationEvent) {
        StompHeaderAccessor sha = StompHeaderAccessor.
                wrap(applicationEvent.getMessage());
        if (sha.getUser() != null) {
            Account account = accountRepository.findByEmail
                    (sha.getUser().getName());
            ServerWarbotGameAgent swga = parties.get(account);
            if (swga != null) {
                swga.sendMessage(new EndMessage("terminate-alone"));
            }
        }

    }

    public boolean haveAlreadyPartyStarted(Account account) {
        if (parties.containsKey(account)) {
            boolean alive = parties.get(account).getAlive().get();
            if (!alive) {
                parties.remove(account);
            }
            return alive;
        }
        return false;
    }

    public boolean constructProcessAndServerWarbotAgent(Account account, LaunchGameCommand lgc) {
        JVMBuilder jvmBuilder = new JVMBuilder();
        try {
            jvmBuilder.addClasspathByClass(WarGame.class)//Warbot
                    .addClasspathByClass(TurtleKit.class)//Madkit/TurtleKit
                    .addClasspathByClass(Application.class)//Fairbot
                    .addClasspathByClass(org.slf4j.Logger.class)//Logger
                    .addClasspathByClass(com.mysql.jdbc.Driver.class)
                    .addClasspathByClass(org.slf4j.impl.StaticLoggerBinder.class)//Logger impl
                    .addClasspathLibrary("libs/*.jar");//All in libs directory WebApp
            //Thread classpath
            if (getClass().getClassLoader() instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) getClass().getClassLoader()).getURLs()) {
                    jvmBuilder.addClasspathLibrary(new File(url.toURI()).getAbsolutePath());
                }
            }
            //System classpath
            if (getClass().getClassLoader() instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) ClassLoader.getSystemClassLoader()).getURLs()) {
                    jvmBuilder.addClasspathLibrary(new File(url.toURI()).getAbsolutePath());
                }
            }

            jvmBuilder.setMainClass(MainWarbot.class)
                    .setInputStream(ProcessBuilder.Redirect.PIPE)
                    .setOutputStream(ProcessBuilder.Redirect.PIPE)
                    .addArgument(JSONInterProcessMessageTranslater.convertIntoMessage(lgc));

            Process p = jvmBuilder.build();
            ServerWarbotGameAgent swga = new ServerWarbotGameAgent
                    (account, p, p.getInputStream(), p.getOutputStream(),
                            templateMessaging);
            parties.put(account, swga);

            Thread thread = new Thread(swga);
            thread.start();
            return true;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void startWebGame(Account account, WebGameSettings settings) throws AlreadyRunningGameException {
        if (haveAlreadyPartyStarted(account))
            throw new AlreadyRunningGameException();

        LaunchGameCommand.LaunchGameCommandBuilder lgcb = new LaunchGameCommand.LaunchGameCommandBuilder();
        LaunchGameCommand lgc = lgcb.setPlayerForTeam1(settings.getIdTeam1()).setPlayerForTeam2(settings.getIdTeam2()).build();

        constructProcessAndServerWarbotAgent(account, lgc);
    }

    @Override
    public void startAgainstIA(Account account, Party party) throws AlreadyRunningGameException {
        if (haveAlreadyPartyStarted(account))
            throw new AlreadyRunningGameException();

        LaunchGameCommand.LaunchGameCommandBuilder lgcb = new LaunchGameCommand.LaunchGameCommandBuilder();
        LaunchGameCommand lgc = lgcb.setPlayerForTeam1(party.getId()).setValueTeam2(LaunchGameCommand.IA_TEAM_RANDOM).build();

        constructProcessAndServerWarbotAgent(account, lgc);
    }


    @Override
    public void startExampleWebGame(Account account) throws AlreadyRunningGameException {
        if (haveAlreadyPartyStarted(account))
            throw new AlreadyRunningGameException();
        LaunchGameCommand.LaunchGameCommandBuilder lgcb = new LaunchGameCommand.LaunchGameCommandBuilder();
        LaunchGameCommand lgc = lgcb.setValueTeam1(LaunchGameCommand.IA_TEAM_RANDOM).setValueTeam2(LaunchGameCommand.IA_TEAM_RANDOM).build();

        constructProcessAndServerWarbotAgent(account, lgc);

    }

    @Override
    public void stopGame(Account account) {
        if (haveAlreadyPartyStarted(account))
            parties.get(account).stop();
    }

    @Override
    public void pauseGame(Account account) {
        if (haveAlreadyPartyStarted(account))
            parties.get(account).sendMessage(new RemoteWarbotCommand(RemoteWarbotCommand.WarbotCommand.PAUSE));
    }


    @Override
    public void preciseAgentFromGame(Account account, String id) {
        if (haveAlreadyPartyStarted(account))
            parties.get(account).sendMessage((id != null) ? new PreciseAgentCommand(id) : new PreciseAgentCommand(PreciseAgentCommand.NONE));
    }

}
