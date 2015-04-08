package edu.warbot.controllers;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.form.PartyForm;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.WebAgent;
import edu.warbot.models.WebCode;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.PartyRepository;
import edu.warbot.repository.WebAgentRepository;
import edu.warbot.repository.WebCodeRepository;
import edu.warbot.scriptcore.script.Script;
import edu.warbot.services.CodeEditorService;
import edu.warbot.services.TeamService;
import edu.warbot.support.web.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.*;

/**
 * Created by BEUGNON on 18/03/2015.
 *
 * @author Sébastien Beugnon
 */
@Controller
@Secured({"ROLE_USER","ROLE_ADMIN"})
public class PartyController implements ApplicationContextAware
{
    final Logger logger = LoggerFactory.getLogger(PartyController.class);

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WebAgentRepository webAgentRepository;

    @Autowired
    private WebCodeRepository webCodeRepository;

    @Autowired
    private CodeEditorService codeEditorService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "party/entity", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Party party(@RequestParam("id") Long id) {
        Assert.notNull(id);
        return partyRepository.findOne(id);
    }


    @RequestMapping(value = "party/create",method = RequestMethod.GET)
    public String createParty(Model model)
    {
        model.addAttribute("form", new PartyForm());
        return "party/create";
    }

    @RequestMapping(value = "party/create",method = RequestMethod.POST)
    public String checkParty(Principal principal,
                             @Valid @ModelAttribute("form") PartyForm partyForm,
                             Errors errors, RedirectAttributes ra)
    {

        Assert.notNull(principal);
        if(errors.hasErrors())
            return "party/create";
        logger.debug("Pass party");
        Account account = accountRepository.findByEmail(principal.getName());
        if(account!=null)
            logger.debug("Found user");

        Party party = partyForm.createParty();
        party.setCreator(account);
        party.getMembers().add(account);

        if(partyRepository.findByName(party.getName()) == null)
        {
            logger.debug("Not found party");
            party = partyRepository.save(party);

            //TODO ADD DEFAULT CODE FOR PARTY

            Map<WarAgentType, StringBuilder> codeAgent = new HashMap<>();

            try {
                Resource[] resources = applicationContext.getResources("classpath:script/python/*");

                if(resources.length == 0)
                    throw new IOException("Probleme avec ressource python");

                for(Resource resource : resources) {
                    Scanner scanner = new Scanner(resource.getFile());
                    StringBuilder sb = new StringBuilder();

                    while(scanner.hasNext())
                    {
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

            for(WebAgent agent : webAgents) {
                WebCode webCode = new WebCode(agent, party);
                webCode.setContent(codeAgent.get(agent.getType()).toString());
                webCodeRepository.save(webCode);
            }
        }
        else
        {
            logger.debug("Found party");
            MessageHelper.addErrorAttribute(ra, "party.fail.name");
            return "party/create";
        }
        MessageHelper.addSuccessAttribute(ra, "party.success");
        ra.addAttribute("id", party.getId());
        return "redirect:/party/show";
    }

    @RequestMapping(value = "party/show", method = RequestMethod.GET)
    public String viewTeam(Principal principal,
                           Model model,
                           @RequestParam(required = true) Long id)
    {
        Party party = partyRepository.findOne(id);
        party.getMembers();
        model.addAttribute("party", party);
        return "teamcode/showTeam";
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }
}
