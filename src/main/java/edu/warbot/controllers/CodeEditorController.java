package edu.warbot.controllers;

import edu.warbot.exceptions.NotFoundEntityException;
import edu.warbot.exceptions.UnauthorisedToEditLockException;
import edu.warbot.exceptions.UnauthorisedToEditNotMemberException;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.PartyRepository;
import edu.warbot.repository.WebAgentRepository;
import edu.warbot.services.CodeEditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created by BEUGNON on 01/04/2015.
 * CodeEditorController, un contrôleur asynchrône pour gérer les requêtes de lecture et d'écriture liées
 * à l'éditeur de code
 * @author Sébastien Beugnon
 */
@Async
@Controller
@Secured({"ROLE_USER","ROLE_ADMIN"})
public class CodeEditorController
{

    @Autowired
    private WebAgentRepository webAgentRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CodeEditorService codeEditorService;



    @SubscribeMapping("/editor/register")
    public void registerUser(Principal principal)
    {
        Assert.notNull(principal);

    }


    @MessageMapping("/editor/list")
    public List<WebAgent> giveListAgentFor(@RequestParam Long idParty)
            throws NotFoundEntityException {
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        return webAgentRepository.findForParty(party);
    }


    @MessageMapping("/editor/get")
    public WebCode getCodeForAgent(
            @RequestParam Long idParty,
            @RequestParam Long idWebAgent)
            throws NotFoundEntityException {
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(idWebAgent);
        Assert.notNull(agent);
        WebCode wc = codeEditorService.getWebCode(party, agent);
        Assert.notNull(agent);
        return wc;
    }

    @MessageMapping("/editor/lock")
    public boolean lock(@RequestParam Long idParty,
                        @RequestParam Long idWebAgent,
                        Principal principal)
    {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(idWebAgent);
        Assert.notNull(agent);
        return true;
    }

    @MessageMapping("/editor/save")
    public boolean save(@RequestParam Long idParty,
                        @RequestParam Long idWebAgent,
                        @RequestParam String content,
                        Principal principal)
    {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(idWebAgent);
        Assert.notNull(agent);
        WebCode code = codeEditorService.getWebCode(party, agent);
        code.setContent(content);
        try {
            codeEditorService.saveWebCode(account, code);
            return true;
        } catch (UnauthorisedToEditNotMemberException | UnauthorisedToEditLockException e) {
            e.printStackTrace();
            return false;
        }
    }

    @MessageMapping("/editor/unlock")
    public boolean unlock(@RequestParam Long idParty,
                          @RequestParam Long idWebAgent,
                          Principal principal)
    {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(idWebAgent);
        Assert.notNull(agent);
        return true;
    }



}
