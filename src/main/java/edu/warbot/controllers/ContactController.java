package edu.warbot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jimmy on 02/05/15.
 */
@Controller
public class ContactController {

    @RequestMapping(value = "fragments.header.contact")
    public String about() {
        return "feedback/contact";
    }
}
