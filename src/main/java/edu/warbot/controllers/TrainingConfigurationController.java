package edu.warbot.controllers;

import com.mongodb.util.JSON;
import edu.warbot.form.TrainingConfigurationForm;
import edu.warbot.models.Account;
import edu.warbot.models.RequestTrainingConfigurationWrapper;
import edu.warbot.models.TrainingAgent;
import edu.warbot.models.TrainingConfiguration;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.TrainingConfigurationRepository;
import edu.warbot.services.TrainingConfigurationService;
import edu.warbot.services.WarbotOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

/**
 * Created by jimmy on 13/04/15.
 */

@Controller
@Secured({"ROLE_USER","ROLE_ADMIN"})
public class TrainingConfigurationController {

    final Logger logger = LoggerFactory.getLogger(TrainingConfigurationController.class);

    @Autowired
    private TrainingConfigurationService trainingConfigurationService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TrainingConfigurationRepository trainingConfigurationRepository;

    @RequestMapping(value = "configuration/list", method = RequestMethod.GET)
    public String listEditor(Model model) {
        Iterable<TrainingConfiguration> trainingConfigurationsList;
        trainingConfigurationsList = trainingConfigurationService.findAll();
        model.addAttribute("trainingConfigurations", trainingConfigurationsList);
        return "configuration-editor/select";
    }

    @RequestMapping(value = "configuration/create", method = RequestMethod.POST)
    public String createEditor(Principal principal, @Valid @ModelAttribute("form") TrainingConfigurationForm tcForm, RedirectAttributes ra) {

        Account account = accountRepository.findByEmail(principal.getName());
        if(account!=null)
            logger.debug("Found user");

        TrainingConfiguration tc = tcForm.createTestZone();
        tc.setCreator(account);
        tc = trainingConfigurationService.createTrainingConfiguration(tc);
        ra.addAttribute("trainingId", tc.getId());
        return "redirect:/configuration/edit";
    }

    @RequestMapping(value = "configuration/edit", method = RequestMethod.GET)
    public String edit(Principal principal, @RequestParam Long trainingId, RedirectAttributes ra) {
        Account account = accountRepository.findByEmail(principal.getName());
        if(account!=null)
            logger.debug("Found user");

        TrainingConfiguration tc;
        tc = trainingConfigurationService.findOne(trainingId);
        ra.addAttribute("trainingId", tc.getId());
        /*if (tc.getCreator().getEmail() != account.getEmail()) {
            return "redirect:/configuration/list";
        }
        else {
            ra.addAttribute("agents", tc.getTrainingAgents());
            return "configuration-editor/editor";
        }*/
        return "configuration-editor/editor";
    }


    @RequestMapping(value = "configuration/update", method = RequestMethod.POST)
    public String update(@RequestParam String agents, @RequestParam Long trainingId) {

        /*TrainingConfiguration tc;
        tc = trainingConfigurationService.findOne(trainingId);
        tc.setTrainingAgents(agents);
        for (TrainingAgent agent : tc.getTrainingAgents()) {
            System.out.println(agent.getName());
        }*/
        System.out.println(agents.toString());
        return "redirect:/configuration/list";

    }

    @RequestMapping(value = "configuration/new",method = RequestMethod.GET)
    public String createEditor(Model model)
    {
        model.addAttribute("formZone", new TrainingConfigurationForm());
        return "configuration-editor/create";
    }




}
