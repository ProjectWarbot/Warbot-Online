package edu.warbot.process.communication.client;

import edu.warbot.process.communication.InterProcessMessage;

import java.util.Map;

/**
 * Created by beugnon on 04/04/15.
 * <p>
 * Class représentant un message indiquant des modifications pour un agent
 *
 * @author beugnon
 */
public class AgentMessage extends InterProcessMessage {

    public final static String HEADER = "agent";

    private final Map<String, Object> content;


    public AgentMessage(Map<String, Object> content) {
        super(HEADER);
        this.content = content;
    }


    public Map<String, Object> getContent() {
        return this.content;
    }
}
