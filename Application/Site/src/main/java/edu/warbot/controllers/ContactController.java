package edu.warbot.controllers;

import edu.warbot.form.ContactForm;
import edu.warbot.models.Account;
import edu.warbot.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by jimmy on 02/05/15.
 */
@Controller
public class ContactController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/contact")
    public String about(Model model, Principal principal) {
        if (principal != null) {
            Account account = accountRepository.findByEmail(principal.getName());
            Assert.notNull(account);
            model.addAttribute("email", account.getEmail());
            model.addAttribute("login", account.getScreenName());
        }
        return "feedback/contact";
    }


    @RequestMapping(value = "/sendcontact", method = RequestMethod.POST)
    public String send(Model model, Principal principal, @Valid @ModelAttribute("form") ContactForm contactForm) {

        String[] mailToSend = contactForm.createMail();
        /*
        SEND EMAIL HERE !!!
         */
        return "feedback/contact";
    }
}
