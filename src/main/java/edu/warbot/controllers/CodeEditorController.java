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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
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
@Controller
@Async
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


    @RequestMapping(value = "editor/", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<WebAgent> giveListAgentFor(@RequestParam Long idParty)
            throws NotFoundEntityException {
        Party party = partyRepository.findOne(idParty);
        if(party==null)
            throw new NotFoundEntityException(Party.class);
        return webAgentRepository.findForParty(party);
    }


    @RequestMapping(value = "editor/get/", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public WebCode getCodeForAgent(
            @RequestParam Long idParty,
            @RequestParam Long idWebAgent)
            throws NotFoundEntityException {
        Party party = partyRepository.findOne(idParty);
        Assert.notNull(party);
        WebAgent agent = webAgentRepository.findOne(idWebAgent);
        Assert.notNull(agent);
        return codeEditorService.getWebCodeReadOnly(party, agent);
    }

    @RequestMapping(value = "editor/lock/", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
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
        return codeEditorService.lockForEdit(account, party, agent);
    }

    @RequestMapping(value = "editor/save/", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
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
        WebCode code = codeEditorService.getWebCodeReadOnly(party,agent);
        code.setContent(content);
        try {
            codeEditorService.saveWebCode(account, code);
            return true;
        } catch (UnauthorisedToEditNotMemberException | UnauthorisedToEditLockException e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping(value = "editor/unlock/", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
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
        return codeEditorService.unlockForEdit(account,party,agent);
    }



}
