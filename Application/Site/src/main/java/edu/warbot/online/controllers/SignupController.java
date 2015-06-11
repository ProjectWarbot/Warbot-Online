package edu.warbot.online.controllers;

import edu.warbot.online.form.SignupForm;
import edu.warbot.online.models.Account;
import edu.warbot.online.repository.AccountRepository;
import edu.warbot.online.services.UserService;
import edu.warbot.online.support.web.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller
public class SignupController {

    private static final String SIGNUP_VIEW_NAME = "signup/signup";

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @Inject
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "signup")
    public String signup(Model model) {
        model.addAttribute(new SignupForm());
        return SIGNUP_VIEW_NAME;
    }

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    public String signup(@Valid @ModelAttribute SignupForm signupForm, Errors errors, RedirectAttributes ra) {
        if (errors.hasErrors()) {
            return SIGNUP_VIEW_NAME;
        }
        if (accountRepository.findByEmail(signupForm.getEmail()) != null) {

            errors.rejectValue("email", "signup.fail.already.used");
            return SIGNUP_VIEW_NAME;
        }

        Account account = signupForm.createAccount();
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account = accountRepository.save(account);


        userService.signin(account);
        // see /WEB-INF/i18n/messages.properties and /WEB-INF/views/homeSignedIn.html
        MessageHelper.addSuccessAttribute(ra, "signup.success");
        return "redirect:/";
    }
}
