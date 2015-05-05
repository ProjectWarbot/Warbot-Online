package edu.warbot.process.game;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.MotherNatureTeam;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.game.WarGameSettings;
import edu.warbot.gui.viewer.WarDefaultViewer;
import edu.warbot.launcher.WarEnvironment;
import edu.warbot.maps.AbstractWarMap;
import edu.warbot.tools.geometry.WarCircle;
import madkit.action.SchedulingAction;
import madkit.kernel.Madkit;
import madkit.message.SchedulingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turtlekit.kernel.TKLauncher;
import turtlekit.kernel.TurtleKit;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by beugnon on 05/04/15.
 *
 * @author beugnon²
 */
public class WebLauncher extends TKLauncher {

    private static Logger logger = LoggerFactory.getLogger(WebLauncher.class);

    private WebGame game;


    public WebLauncher(WebGame game) {
        this.game = game;
    }

    @Override
    protected void activate() {
        logger.trace("entrée dans activate");
        super.activate();
        logger.trace("sortie dans activate");
    }

    protected void init2Properties() {
        WarGameSettings settings = getGame().getSettings();
        this.setLogLevel(settings.getLogLevel());
        this.setMadkitProperty(Madkit.LevelOption.agentLogLevel, settings.getLogLevel().toString());
        this.setMadkitProperty(Madkit.LevelOption.guiLogLevel, settings.getLogLevel().toString());
        this.setMadkitProperty(Madkit.LevelOption.kernelLogLevel, settings.getLogLevel().toString());
        this.setMadkitProperty(Madkit.LevelOption.madkitLogLevel, settings.getLogLevel().toString());
        this.setMadkitProperty(Madkit.LevelOption.networkLogLevel, settings.getLogLevel().toString());
        super.initProperties();
        this.setMadkitProperty(Madkit.BooleanOption.console, "false");
        this.setMadkitProperty(TurtleKit.Option.envWidth,
                String.valueOf(Double.valueOf(getGame().getMap().getWidth()).intValue()));
        this.setMadkitProperty(TurtleKit.Option.envHeight, String.valueOf(Double.valueOf(getGame().getMap().getHeight()).intValue()));
        this.setMadkitProperty(TurtleKit.Option.viewers, WarDefaultViewer.class.getName());
        this.setMadkitProperty(TurtleKit.Option.scheduler, WebScheduler.class.getName());
        this.setMadkitProperty(TurtleKit.Option.environment, WarEnvironment.class.getName());
    }

    @Override
    protected void createSimulationInstance() {
        logger.trace("entrée dans createSimulationInstance");

        init2Properties();
        this.launchAgent(this.getMadkitProperty(TurtleKit.Option.environment));
        this.launchConfigTurtles();
        //VIEWER
        if (getGame().getSettings().getSituationLoader() == null) {
            this.launchAllAgents();
        } else
        {
            //TODO SITUATION LOADER
        }
        //SCHEDULER
        WebScheduler webScheduler = new WebScheduler(getGame());
        launchAgent(webScheduler);

        this.sendMessage(this.getMadkitProperty(TurtleKit.Option.community), "engine", "scheduler", new SchedulingMessage(SchedulingAction.RUN, new Object[0]));
        getGame().setGameStarted();

        logger.trace("sortie dans createSimulationInstance");
    }

    public WebGame getGame() {
        return game;
    }

    protected void launchAllAgents() {
        WarGame game = getGame();
        AbstractWarMap map = game.getMap();
        ArrayList teamsPositions = map.getTeamsPositions();
        int teamCount = 0;
        MotherNatureTeam motherNatureTeam = game.getMotherNatureTeam();

        try {
            for (Team t : game.getPlayerTeams()) {
                WarCircle selectedPosition = (WarCircle) ((ArrayList) teamsPositions.get(teamCount)).get((new Random()).nextInt(((ArrayList) teamsPositions.get(teamCount)).size()));

                for (WarAgentType agentType : WarAgentType.values()) {


                    for (int e = 0; e < game.getSettings().getNbAgentOfType(agentType); ++e) {
                        try {
                            ControllableWarAgent e1 = t.instantiateNewControllableWarAgent(agentType.toString());
                            this.launchAgent(e1);
                            e1.setRandomPositionInCircle(selectedPosition);
                        } catch (NoSuchMethodException |
                                ClassNotFoundException |
                                InvocationTargetException var16) {
                            logger.error("Erreur lors de l\'instanciation de l\'agent." +
                                    " Type non reconnu : " + agentType);
                            var16.printStackTrace();
                        }

                        motherNatureTeam.createAndLaunchNewResource(game.getMap(), this, WarAgentType.WarFood);
                    }
                }
                teamCount++;
            }
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InstantiationException var17) {
            logger.error("Erreur lors de l\'instanciation des classes à partir des données XML");
        }

    }
}
