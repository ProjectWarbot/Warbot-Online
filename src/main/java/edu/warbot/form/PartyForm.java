package edu.warbot.form;

import edu.warbot.models.Party;
import edu.warbot.scriptcore.interpreter.ScriptInterpreterLangage;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by BEUGNON on 18/03/2015.
 *
 * @author Sébastien Beugnon
 */
public class PartyForm
{

    private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";

    @NotBlank(message = PartyForm.NOT_BLANK_MESSAGE)
    private String teamName;

    @NotBlank(message = PartyForm.NOT_BLANK_MESSAGE)
    private ScriptInterpreterLangage language;

    public Party createParty()
    {
        Party p = new Party(teamName, language);
        return p;
    }
}
