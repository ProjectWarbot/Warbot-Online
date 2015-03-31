package edu.warbot.controllers;

import edu.warbot.form.SignupForm;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.form.PartyForm;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.PartyRepository;
import edu.warbot.services.UserService;
import edu.warbot.support.web.MessageHelper;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by BEUGNON on 18/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Controller
@Secured({"ROLE_USER","ROLE_ADMIN"})
public class PartyController
{
    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private AccountRepository accountRepository;

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

    @RequestMapping(value = "party/create",method = RequestMethod.POST)
    public String checkParty(Principal principal,@Valid @ModelAttribute PartyForm partyForm, Errors errors, RedirectAttributes ra)
    {
        Assert.notNull(principal);
        if(errors.hasErrors())
            return "party/create";
        Account account = accountRepository.findByEmail(principal.getName());
        Party party = partyForm.createParty();
        party.setCreator(account);
        party.getMembers().add(account);
        if(partyRepository.findByName(party.getName())!=null)
            party = partyRepository.save(party);
        else
        {
            MessageHelper.addErrorAttribute(ra, "party.fail.name");
            return "party/create";
        }
        MessageHelper.addErrorAttribute(ra, "party.success");
        return "redirect://party/show";
    }
}
