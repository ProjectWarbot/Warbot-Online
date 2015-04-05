package edu.warbot.online.socket;

import edu.warbot.models.Account;
import edu.warbot.online.messaging.WebGameSettings;
import edu.warbot.repository.AccountRepository;
import edu.warbot.services.WebGameService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import java.security.Principal;

@Controller
@Secured({"ROLE_USER","ROLE_ADMIN"})
public class WebGameSocket {


    private static final Log logger = LogFactory.getLog(WebGameSocket.class);

    @Autowired
    private WebGameService webGameService;

    @Autowired
    private AccountRepository accountRepository;


    @SubscribeMapping("/game/register")
    public void registerUser(Principal principal)
    {
        Assert.notNull(principal);
        //TODO SUBSCRIBE USER
    }


    @MessageMapping("/game/start")
    public void startGame(Principal principal, WebGameSettings settings)
    {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        webGameService.startExampleWebGame(account);
    }
}
