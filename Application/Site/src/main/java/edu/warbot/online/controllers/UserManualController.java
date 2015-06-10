package edu.warbot.online.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * Created by jimmy on 31/05/15.
 */

@Controller
public class UserManualController {

    @RequestMapping(value = "/userManual")
    public String manual(Model model, Principal principal) {

        return "feedback/userManual";
    }
}
