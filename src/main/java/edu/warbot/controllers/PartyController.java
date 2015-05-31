package edu.warbot.controllers;


import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.exceptions.UnauthorisedToEditLockException;
import edu.warbot.exceptions.UnauthorisedToEditNotMemberException;
import edu.warbot.form.PartyForm;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.WebAgentRepository;
import edu.warbot.services.CodeEditorService;
import edu.warbot.services.WarbotOnlineService;
import edu.warbot.support.web.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by BEUGNON on 18/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class PartyController implements ApplicationContextAware {
    final Logger logger = LoggerFactory.getLogger(PartyController.class);


    @Autowired
    private WarbotOnlineService warbotOnlineService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebAgentRepository webAgentRepository;

    @Autowired
    private CodeEditorService codeEditorService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "party/entity", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Party party(@RequestParam("id") Long id) {
        Assert.notNull(id);
        return warbotOnlineService.findPartyById(id);
    }


    @RequestMapping(value = "party/create", method = RequestMethod.GET)
    public String createParty(Model model) {
        model.addAttribute("form", new PartyForm());
        return "party/create";
    }

    @RequestMapping(value = "party/create", method = RequestMethod.POST)
    public String checkParty(Principal principal,
                             @Valid @ModelAttribute("form") PartyForm partyForm,
                             Errors errors, RedirectAttributes ra) {

        Assert.notNull(principal);
        if (errors.hasErrors())
            return "party/create";
        logger.debug("Pass party");
        Account account = accountRepository.findByEmail(principal.getName());
        if (account != null)
            logger.debug("Found user");

        Party party = partyForm.createParty();
        party.setCreator(account);
        if (warbotOnlineService.findPartyByName(party.getName()) == null) {
            logger.debug("Not found party");
            party = warbotOnlineService.createParty(party);

            Map<WarAgentType, StringBuilder> codeAgent = new HashMap<>();

            try {
                Resource[] resources = applicationContext.getResources("classpath:script/python/*");

                if (resources.length == 0)
                    throw new IOException("Problème avec ressource python");

                for (Resource resource : resources) {
                    Scanner scanner = new Scanner(resource.getFile());
                    StringBuilder sb = new StringBuilder();

                    while (scanner.hasNext()) {
                        sb.append(scanner.nextLine());
                        sb.append("\n");
                    }
                    scanner.close();
                    int index = resource.getFilename().indexOf(".");
                    String typeAgent = resource.getFilename().substring(0, index);
                    codeAgent.put(WarAgentType.valueOf(typeAgent), sb);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            List<WebAgent> webAgents = webAgentRepository.findAllStarter();
            for (WebAgent agent : webAgents) {
                WebCode webCode = new WebCode(agent, party);
                webCode.setContent(codeAgent.get(agent.getType()).toString());
                try {
                    codeEditorService.saveWebCode(account, webCode);
                } catch (UnauthorisedToEditLockException e) {
                    e.printStackTrace();
                } catch (UnauthorisedToEditNotMemberException e) {
                    e.printStackTrace();
                }

            }
        } else {
            logger.debug("Found party");
            MessageHelper.addErrorAttribute(ra, "party.fail.name");
            return "party/create";
        }
        warbotOnlineService.addMember(party, account);
        MessageHelper.addSuccessAttribute(ra, "party.success");
        ra.addAttribute("idParty", party.getId());
        return "redirect:/teamcode";
    }

    @RequestMapping(value = "party/show", method = RequestMethod.GET)
    public String viewTeam(Principal principal,
                           Model model,
                           @RequestParam(required = true) Long id) {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Party party = warbotOnlineService.findPartyById(id);
        Assert.notNull(party);
        model.addAttribute("account", account);
        model.addAttribute("party", party);
        return "party/showParty";
    }

    @RequestMapping(value = "party/delete", method = RequestMethod.GET)
    public String delete(Principal principal, Model model, @RequestParam(required = true) Long id) {
        Assert.notNull(principal);
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        Party party = warbotOnlineService.findPartyById(id);
        Assert.notNull(party);
        if (party.getCreator().equals(account)) {
            codeEditorService.deleteCodeForParty(id);
            warbotOnlineService.deleteParty(id);
            MessageHelper.addSuccessAttribute(model, "party.delete", party.getName());

        } else {
            MessageHelper.addErrorAttribute(model, "party.not.member", party.getName());
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/partylist", method = RequestMethod.GET)
    public String partylist(Model model, Principal principal) {
        Assert.notNull(principal);
        Iterable<Party> partyList;
        partyList = warbotOnlineService.findAllParty();
        Iterable<Party> myParties;
        Account account = accountRepository.findByEmail(principal.getName());
        myParties = warbotOnlineService.findPartyByCreator(account);
        model.addAttribute("parties", partyList);
        model.addAttribute("myParties", myParties);
        return "party/list";
    }

    @RequestMapping(value = "/party/add/members")
    public String addMember(Principal principal, RedirectAttributes ra,
                            @RequestParam Long idParty, @RequestParam Long idUser) {
        Account account = accountRepository.findByEmail(principal.getName());
        Party party = warbotOnlineService.findPartyById(idParty);
        if (party.getCreator().equals(account)) {
            Account member = accountRepository.findOne(idUser);
            Assert.notNull(member);
            if (!party.getMembers().contains(member))
                warbotOnlineService.addMember(party, member);
        }
        ra.addAttribute("id", party.getId());
        return "redirect:/party/show";
    }

    @RequestMapping(value = "/party/remove/members")
    public String removeMember(Principal principal, RedirectAttributes ra,
                               @RequestParam Long idParty, @RequestParam Long idUser) {
        Account account = accountRepository.findByEmail(principal.getName());
        Party party = warbotOnlineService.findPartyById(idParty);

        if (party.getCreator().equals(account)) {
            Account member = accountRepository.findOne(idUser);
            Assert.notNull(member);
            if (party.getMembers().contains(member))
                warbotOnlineService.removeMember(party, member);
        }
        ra.addAttribute("id", party.getId());
        return "redirect:/party/show";
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
