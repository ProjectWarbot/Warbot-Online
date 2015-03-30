package edu.warbot.controllers;

import edu.warbot.form.SignupForm;
import edu.warbot.models.Party;
import edu.warbot.form.PartyForm;
import edu.warbot.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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


    @RequestMapping(value = "party/create",method = RequestMethod.GET)
    public String createParty(Model model)
    {
        model.addAttribute("form",new PartyForm());
        return "party/create";
    }

    public String checkParty(@Valid @ModelAttribute PartyForm signupForm, Errors errors, RedirectAttributes ra) {
    {
        if(errors.hasErrors())
            return "party/create";
    }
        return "party/show";
    }
}
