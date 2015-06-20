package edu.warbot.online.process.communication.server;

import edu.warbot.online.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 21/04/15.
 */
public class PreciseAgentCommand extends InterProcessMessage {

    public final static String HEADER = "PreciseAgentCommand";

    public final static String NONE = "none";
    private final String agentId;

    /**
     * Constructeur
     */
    public PreciseAgentCommand(String agentID) {
        super(HEADER);
        this.agentId = agentID;
    }

    public String getAgentId() {
        return this.agentId;
    }

    public boolean isDeactivate() {
        return this.agentId.equals("none");
    }


    public static class PreciseAgentCommandBuilder {

        private String agentId;

        public PreciseAgentCommandBuilder() {

        }

        public PreciseAgentCommandBuilder deactivate() {
            this.agentId = NONE;
            return this;
        }

        public PreciseAgentCommand build() {
            return new PreciseAgentCommand(this.agentId);
        }
    }

}
