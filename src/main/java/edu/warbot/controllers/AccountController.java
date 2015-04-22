package edu.warbot.controllers;

import java.security.Principal;

import edu.warbot.models.Account;
import edu.warbot.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @RequestMapping(value = "account/profile", method = RequestMethod.GET)
    public String profile(Model model,Principal principal)
    {
        Assert.notNull(principal);
        Account account = accounts(principal);
        model.addAttribute("account",account);
        return "account/profile";
    }
}
