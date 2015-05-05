package edu.warbot.process.game;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.game.WarGameSettings;
import edu.warbot.online.logs.GameLog;
import edu.warbot.online.logs.RGB;
import edu.warbot.online.logs.entity.EntityLog;
import edu.warbot.process.communication.InterProcessMessage;
import edu.warbot.process.communication.WarbotProcessSender;
import edu.warbot.process.communication.client.AgentMessage;
import edu.warbot.process.communication.client.EndMessage;
import edu.warbot.process.communication.client.InitMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BEUGNON on 11/03/2015.
 * <p>
 * Classe représentant une partie jouée sur le web
 * en streaming
 *
 * @author beugnon
 */
public class WebGame extends WarGame {

    private GameLog gameLog;

    private WarbotGameAgent gameAgent;

    private boolean firstCall;
    private int tick;

    public WebGame(WarbotGameAgent gameAgent, WarGameSettings settings) {
        super(settings);
        tick = 0;
        this.firstCall = true;
        this.gameAgent = gameAgent;
        this.gameLog = new GameLog();
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    @Override
    public void setGameStarted() {
        super.setGameStarted();
    }

    @Override
    public void doAfterEachTick() {
        super.doAfterEachTick();
        if (isFirstCall()) {
            sendInitMessage();
            firstCall = false;
        }

        getGameLog().obsolete();

        for (Team t : getPlayerTeams()) {
            for (WarAgent a : t.getAllAgents()) {

                if (a instanceof ControllableWarAgent) {
                    Map<String, Object> map = (this.getGameLog().addOrUpdateControllableEntity((ControllableWarAgent) a));

                    sendMessage(new AgentMessage(map));
                } else {
                    Map<String, Object> map = this.getGameLog().addOrUpdateEntity(a);
                    sendMessage(new AgentMessage(map));

                }
            }
        }

        for (WarAgent a : getMotherNatureTeam().getAllAgents()) {
            Map<String, Object> map = this.getGameLog().addOrUpdateEntity(a);
            sendMessage(new AgentMessage(map));
        }

        List<EntityLog> agentDead = new ArrayList<>();

        for (EntityLog el : this.getGameLog().getEntityLog().values())
            if (!el.isUpdated())
                agentDead.add(el);

        for (EntityLog el : agentDead) {
            this.getGameLog().getEntityLog().remove(el.getName());
            Map<String, Object> map = new HashMap<>();
            map.put("name", el.getName());
            map.put("state", -1);
            sendMessage(new AgentMessage(map));
        }

        if (getGameMode().getEndCondition().isGameEnded())
            setGameOver();
    }

    protected boolean isFirstCall() {
        return firstCall;
    }

    protected void sendInitMessage() {
        //Send init message
        Map<String, Object> content = new HashMap<>();

        //Prepare Environment Variables for client
        Map<String, Object> environment = new HashMap<>();
        environment.put("width", getSettings().getSelectedMap().getWidth());
        environment.put("height", getSettings().getSelectedMap().getHeight());
        environment.put("mapName", getSettings().getSelectedMap().getName());
        //TODO trouver un autre moyen d'envoyer les limites de la carte
        // environment.put("walls",getSettings().getSelectedMap().getMapForbidArea());

        content.put("environment", environment);

        //Prepare Team Variables for client
        List<Map<String, Object>> teams = new ArrayList<>();
        for (Team t : getAllTeams()) {
            Map<String, Object> team = new HashMap<>();
            team.put("name", t.getName());


            team.put("color", (new RGB(t.getColor().getRed(), t.getColor().getGreen(), t.getColor().getRed())).toMap());
            teams.add(team);
        }
        content.put("teams", teams);

        //Prepare Agent Variables for client
        List<Map<String, Object>> agents = new ArrayList<>();
        for (Team t : getAllTeams()) {
            for (WarAgent a : t.getAllAgents()) {
                if (a instanceof ControllableWarAgent)
                    agents.add(this.getGameLog().addControllableAgent((ControllableWarAgent) a));

                else
                    agents.add(this.getGameLog().addEntity(a));

            }

        }
        content.put("agents", agents);

        //Send message
        sendMessage(new InitMessage(content));
    }

    @Override
    public void setGameOver() {
        super.setGameOver();
        sendMessage(new EndMessage("game-end"));
        this.gameAgent.getAlive().set(false);
    }

    public void pauseGame() {

    }

    public void restartGame() {

    }

    public void stopGame() {
        super.stopGame();
    }

    public void sendMessage(InterProcessMessage cm) {
        getMessageSender().pushMessage(cm);
    }


    public WarbotProcessSender getMessageSender() {
        return this.gameAgent.getSender();
    }


    public GameLog getGameLog() {
        return gameLog;
    }
}