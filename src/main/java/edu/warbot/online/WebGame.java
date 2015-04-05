package edu.warbot.online;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.game.WarGameSettings;
import edu.warbot.online.logs.GameLog;
import edu.warbot.online.logs.RGB;
import edu.warbot.online.messaging.AgentMessage;
import edu.warbot.online.messaging.ClassicMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.util.MimeTypeUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BEUGNON on 11/03/2015.
 *
 * Classe représentant une partie jouée sur le web
 * en streaming
 *
 * @author beugnon
 */
public class WebGame extends WarGame
{
    private final String user;

    private GameLog gameLog;

    private SimpMessageSendingOperations messagingTemplate;


    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    private int tick;

    public WebGame(String user,SimpMessageSendingOperations messagingTemplate, WarGameSettings settings)
    {
        super(settings);
        tick = 0;
        this.user = user;
        this.messagingTemplate = messagingTemplate;
        this.gameLog = new GameLog();
    }

    @Override
    public void setGameStarted() {
        super.setGameStarted();
        //Send init message
        Map<String,Object> content = new HashMap<>();

        //Prepare Environment Variables for client
        Map<String,Object> environment = new HashMap<>();
        environment.put("width",getSettings().getSelectedMap().getWidth());
        environment.put("height",getSettings().getSelectedMap().getHeight());
        environment.put("mapName",getSettings().getSelectedMap().getName());
        environment.put("walls",getSettings().getSelectedMap().getMapForbidArea());

        content.put("environment",environment);

        //Prepare Team Variables for client
        List<Map<String,Object>> teams = new ArrayList<>();
        for(Team t : getAllTeams())
        {
            Map<String,Object> team = new HashMap<>();
            team.put("name",t.getName());
            team.put("color",(new RGB(t.getColor().getRed(),t.getColor().getGreen(),t.getColor().getRed())).toString());
            teams.add(team);
        }
        content.put("teams", teams);

        //Prepare Agent Variables for client
        List<Map<String,Object>> agents = new ArrayList<>();
        for(Team t : getAllTeams())
        {
            for(WarAgent a : t.getAllAgents())
                if(a instanceof ControllableWarAgent)
                    agents.add(this.getGameLog().addOrUpdateControllableEntity((ControllableWarAgent) a));
                else
                    agents.add(this.getGameLog().addOrUpdateEntity(a));

        }
        content.put("agents", agents);

        //Send message
        sendMessage(new ClassicMessage("init", content));
    }

    @Override
    public void doAfterEachTick()
    {
        super.doAfterEachTick();

        for (Team t : getPlayerTeams())
        {
            for(WarAgent a : t.getAllAgents())
                if(a instanceof ControllableWarAgent)
                    sendMessage(new AgentMessage(this.getGameLog().addOrUpdateControllableEntity((ControllableWarAgent) a)));
                else
                    sendMessage(new AgentMessage(this.getGameLog().addOrUpdateEntity(a)));

        }

        for(WarAgent a : getMotherNatureTeam().getAllAgents())
            sendMessage(new AgentMessage(this.getGameLog().addOrUpdateEntity(a)));


        if(getGameMode().getEndCondition().isGameEnded())
            setGameOver();
    }

    @Override
    public void setGameOver()
    {
        super.setGameOver();
        sendMessage(new ClassicMessage("end","end game"));
    }




    public void sendMessage(ClassicMessage cm)
    {
        Map<String, Object> map = new HashMap<>();
        map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);

        this.messagingTemplate.
                convertAndSendToUser
                        (user, "/queue/game", cm,map);
    }

    public GameLog getGameLog() {
        return gameLog;
    }
}