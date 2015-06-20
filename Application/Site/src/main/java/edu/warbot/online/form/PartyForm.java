package edu.warbot.online.form;

import edu.warbot.online.models.Party;
import edu.warbot.scriptcore.interpreter.ScriptInterpreterLanguage;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by BEUGNON on 18/03/2015.
 *
 * @author Sébastien Beugnon
 */
public class PartyForm {

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @NotBlank(message = PartyForm.NOT_BLANK_MESSAGE)
    public String teamName;

    @NotBlank(message = PartyForm.NOT_BLANK_MESSAGE)
    public String language;

    public Party createParty() {
        Party p = new Party(teamName, ScriptInterpreterLanguage.valueOf(language));
        return p;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
