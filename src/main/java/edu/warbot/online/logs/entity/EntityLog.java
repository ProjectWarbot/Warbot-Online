package edu.warbot.online.logs.entity;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 *
 * @author beugnon
 */
public class EntityLog implements Comparable<EntityLog>
{
    private String name;

    private WarAgentType type;

    private String team;

    private double angle;

    private double x;

    private double y;

    private int state;

    public EntityLog(String name)
    {
        this.name = name;
        this.team = null;
        this.angle = 0;

        this.x = 0;
        this.y = 0;
        this.type = null;
        this.state = 1;

    }

    public Map<String,Object> update(WarAgent wa)
    {
        Map<String, Object> map = new HashMap<>();
        //key
        map.put("name",name);
        if(x != wa.getX()) {
            x = wa.getX();
            map.put("x",x);
        }

        if(y != wa.getY()) {
            y = wa.getY();
            map.put("y",y);
        }

        if(type != null)//Only one time
        {
            type = wa.getType();
            map.put("type",type);

        }

        if(team != null)
        {
            team = wa.getTeamName();
            map.put("team",team);
        }

        if(angle != wa.getHeading())
        {
            angle = wa.getHeading();
            map.put("angle",angle);
        }

        if(state == 1)//Was alive
        {
            map.put("state",state);
            state = 0;
        }

        if(wa.getDyingStep() > Team.MAX_DYING_STEP)
        {
            state = -1;
            map.put("state",state);
        }

        return map;
    }

    public Map<String,Object> update(ControllableWarAgent wa)
    {
        return update((WarAgent) wa);
    }

    public Map<String,Object> getCurrentState()
    {
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("x",x);
        map.put("y",y);
        map.put("state",state);
        map.put("team",team);
        map.put("angle",angle);
        return map;
    }

    @Override
    public int compareTo(EntityLog entityLog) {
        return name.compareTo(entityLog.name);
    }

    public boolean isDead() {
        return state==-1;
    }
}
