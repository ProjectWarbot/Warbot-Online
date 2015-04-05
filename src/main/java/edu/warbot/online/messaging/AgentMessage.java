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
    public AgentMessage(Map<String,Object> content)
    {
        super("agent", content);
    }

    @Override
    public Map<String,Object> getContent()
    {
        return (Map<String,Object>) super.getContent();
    }
}
