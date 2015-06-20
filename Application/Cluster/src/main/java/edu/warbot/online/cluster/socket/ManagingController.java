package edu.warbot.online.cluster.socket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by beugnon on 11/06/15.
 */
@Controller
public class ManagingController {


    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "home";
    }
}
