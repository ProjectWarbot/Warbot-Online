package edu.warbot.online;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.MotherNatureTeam;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.game.WarGameSettings;
import edu.warbot.gui.viewer.WarDefaultViewer;
import edu.warbot.launcher.WarEnvironment;
import edu.warbot.launcher.WarLauncher;
import edu.warbot.launcher.WarMain;
import edu.warbot.launcher.WarScheduler;
import edu.warbot.maps.AbstractWarMap;
import edu.warbot.tools.geometry.WarCircle;
import madkit.action.SchedulingAction;
import madkit.kernel.Madkit;
import madkit.message.SchedulingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turtlekit.kernel.TurtleKit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by beugnon on 05/04/15.
 *
 * @author beugnon²
 */
public class WebLauncher extends WarLauncher {

    private static Logger logger = LoggerFactory.getLogger(WebLauncher.class);
    private WebGame game;


    public WebLauncher(WebGame game)
    {
        this.game = game;
        logger.debug("Start WebLauncher");
    }

    @Override
    protected void activate() {
        logger.info("Start WebLauncher");
    }

    @Override
    protected void createSimulationInstance() {
        WarGameSettings settings = getGame().getSettings();
        this.setLogLevel(settings.getLogLevel());
        this.setMadkitProperty(Madkit.LevelOption.agentLogLevel, settings.getLogLevel().toString());
        this.setMadkitProperty(Madkit.LevelOption.guiLogLevel, settings.getLogLevel().toString());
        this.setMadkitProperty(Madkit.LevelOption.kernelLogLevel, settings.getLogLevel().toString());
        this.setMadkitProperty(Madkit.LevelOption.madkitLogLevel, settings.getLogLevel().toString());
        this.setMadkitProperty(Madkit.LevelOption.networkLogLevel, settings.getLogLevel().toString());
        this.initProperties();
        this.setMadkitProperty(TurtleKit.Option.envWidth,
                String.valueOf(Double.valueOf(getGame().getMap().getWidth()).intValue()));
        this.setMadkitProperty(TurtleKit.Option.envHeight, String.valueOf(Double.valueOf(getGame().getMap().getHeight()).intValue()));
        this.setMadkitProperty(TurtleKit.Option.viewers,null);
        this.setMadkitProperty(TurtleKit.Option.scheduler, WarScheduler.class.getName());
        this.setMadkitProperty(TurtleKit.Option.environment, WarEnvironment.class.getName());
        super.createSimulationInstance();
        if(settings.getSituationLoader() == null) {
            this.launchAllAgents();
        } else {
            settings.getSituationLoader().launchAllAgentsFromXmlSituationFile(this);
        }

        this.sendMessage(this.getMadkitProperty(TurtleKit.Option.community), "engine", "scheduler", new SchedulingMessage(SchedulingAction.RUN, new Object[0]));
        getGame().setGameStarted();
    }

    public WebGame getGame() {
        return game;
    }

    protected void launchAllAgents() {
        WarGame game = getGame();
        ArrayList playerTeams = game.getPlayerTeams();
        AbstractWarMap map = game.getMap();
        ArrayList teamsPositions = map.getTeamsPositions();
        int teamCount = 0;
        MotherNatureTeam motherNatureTeam = game.getMotherNatureTeam();

        try {
            for(Iterator var8 = playerTeams.iterator(); var8.hasNext(); ++teamCount) {
                Team t = (Team)var8.next();
                WarCircle selectedPosition = (WarCircle)((ArrayList)teamsPositions.get(teamCount)).get((new Random()).nextInt(((ArrayList)teamsPositions.get(teamCount)).size()));
                WarAgentType[] var11 = WarAgentType.values();
                int var12 = var11.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    WarAgentType agentType = var11[var13];

                    for(int e = 0; e < game.getSettings().getNbAgentOfType(agentType); ++e) {
                        try {
                            ControllableWarAgent e1 = t.instantiateNewControllableWarAgent(agentType.toString());
                            this.launchAgent(e1);
                            e1.setRandomPositionInCircle(selectedPosition);
                        } catch (NoSuchMethodException |
                                ClassNotFoundException |
                                InvocationTargetException var16) {
                            logger.error("Erreur lors de l\'instanciation de l\'agent." +
                                    " Type non reconnu : " + agentType,var16);
                            var16.printStackTrace();
                        }

                        motherNatureTeam.createAndLaunchNewResource(game.getMap(), this, WarAgentType.WarFood);
                    }
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InstantiationException var17) {
            logger.error("Erreur lors de l\'instanciation des classes à partir des données XML", var17);
        }

    }
}
