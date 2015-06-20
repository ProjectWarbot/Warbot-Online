package edu.warbot.online.process.communication.server;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.brains.WarBrain;
import edu.warbot.scriptcore.interpreter.ScriptInterpreterLanguage;
import edu.warbot.scriptcore.script.Script;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by beugnon on 29/04/15.
 */
public class CrossProcessTeam implements Serializable {

    private String teamName;

    private HashMap<String, Class<? extends WarBrain>> brainByAgent;

    private HashMap<WarAgentType, Script> scriptByAgent;

    private ScriptInterpreterLanguage language = null;

    public CrossProcessTeam() {
        this.brainByAgent = new HashMap<>();
        this.scriptByAgent = new HashMap<>();
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public HashMap<String, Class<? extends WarBrain>> getBrainByAgent() {
        return brainByAgent;
    }

    public void setBrainByAgent(HashMap<String, Class<? extends WarBrain>> brainByAgent) {
        this.brainByAgent = brainByAgent;
    }

    public HashMap<WarAgentType, Script> getScriptByAgent() {
        return scriptByAgent;
    }

    public void setScriptByAgent(HashMap<WarAgentType, Script> scriptByAgent) {
        this.scriptByAgent = scriptByAgent;
    }

    public ScriptInterpreterLanguage getLanguage() {
        return language;
    }

    public void setLanguage(ScriptInterpreterLanguage language) {
        this.language = language;
    }
}
