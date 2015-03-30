package edu.warbot.party;

import edu.warbot.account.Account;
import edu.warbot.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by BEUGNON on 18/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Controller
@Secured("ROLE_USER")
public class PartyController
{
    @Autowired
    private PartyRepository partyRepository;

    @RequestMapping(value = "party/show", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Party party(@RequestParam("id") Long id) {
        Assert.notNull(id);
        return null;
    }

    public String createParty(Model model)
    {
        model.addAttribute("form",new PartyForm());
        return "party/create";
    }

}
