package edu.warbot.online.messaging;

import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 *
 * Class représentant un message indiquant des modifications pour un agent
 *
 * @author beugnon
 */
public class AgentMessage extends ClassicMessage
{

    private final Map<String, Object> content;



    public AgentMessage(Map<String,Object> content)
    {
        super("agent");
        this.content = content;
    }


    public Map<String,Object> getContent()
    {
        return this.content;
    }
}
