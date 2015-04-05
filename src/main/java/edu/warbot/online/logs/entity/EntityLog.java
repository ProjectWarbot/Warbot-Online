package edu.warbot.online.logs.entity;

import edu.warbot.agents.ControllableWarAgent;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.game.Team;
import edu.warbot.online.logs.RGB;

import java.util.Map;
import java.util.HashMap;

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

    private RGB colorDebug;

    private String messageDebug;

    private int state;

    public EntityLog(String name)
    {
        this.name = name;
        this.state = 1;
    }

    public Map<String,Object> update(ControllableWarAgent wa)
    {
        Map<String, Object> map = new HashMap<>();
        //key
        map.put("name",name);
        if(x != wa.getX())
        {
            x = wa.getX();
            map.put("x",x);
        }

        if(y != wa.getY())
        {
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
            angle = wa.getHealth();
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

        if(colorDebug.r != wa.getDebugStringColor().getRed()
                || colorDebug.g != wa.getDebugStringColor().getGreen()
                || colorDebug.b != wa.getDebugStringColor().getBlue()
                )
        {
            colorDebug.r = wa.getDebugStringColor().getRed();
            colorDebug.g = wa.getDebugStringColor().getGreen();
            colorDebug.b = wa.getDebugStringColor().getBlue();
            map.put("colorDebug",colorDebug.toString());
        }

        if(!messageDebug.equals(wa.getDebugString()))
        {
            messageDebug = wa.getDebugString();
            map.put("messageDebug",messageDebug);
        }

        return map;
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
        if(colorDebug!=null)
            map.put("colorDebug",colorDebug.toString());
        map.put("messageDebug",messageDebug);
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
