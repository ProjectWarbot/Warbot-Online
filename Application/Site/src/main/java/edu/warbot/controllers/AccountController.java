package edu.warbot.controllers;

import edu.warbot.models.Account;
import edu.warbot.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * @author Sébastien Beugnon
 * @author Jimmy Lopez
 */

@Controller
@Secured("ROLE_USER")
class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    private Account accounts(Principal principal) {
        Assert.notNull(principal);
        return accountRepository.findByEmail(principal.getName());
    }

    @RequestMapping(value = "account/profil", method = RequestMethod.GET)
    public String profile(Model model, Principal principal) {
        Assert.notNull(principal);
        Account account = accounts(principal);
        model.addAttribute("account", account);
        return "account/private";
    }

    @RequestMapping(value = "account/userProfil", method = RequestMethod.GET)
    public String userProfile(Model model, Principal principal, @RequestParam Long id) {
        Account account = accountRepository.findOne(id);
        Assert.notNull(account);
        if (account.getEmail().equals(principal.getName()))
            return "redirect:/account/profil";
        model.addAttribute("account", account);
        return "account/public";
    }
}
