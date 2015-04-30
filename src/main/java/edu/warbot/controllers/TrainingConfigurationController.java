package edu.warbot.controllers;

import edu.warbot.form.TrainingConfigurationForm;
import edu.warbot.models.Account;
import edu.warbot.models.TrainingConfiguration;
import edu.warbot.repository.AccountRepository;
import edu.warbot.repository.TrainingConfigurationRepository;
import edu.warbot.services.TrainingConfigurationService;
import edu.warbot.services.WarbotOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

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
    public String createEditor(Principal principal, @Valid @ModelAttribute("form") TrainingConfigurationForm tcForm) {

        Account account = accountRepository.findByEmail(principal.getName());
        if(account!=null)
            logger.debug("Found user");

        TrainingConfiguration tc = tcForm.createTestZone();
        tc.setCreator(account);
        tc = trainingConfigurationService.createTrainingConfiguration(tc);
        return "configuration-editor/editor";
    }

    @RequestMapping(value = "configuration/edit", method = RequestMethod.POST)
    public String createEditor(Principal principal, Long trainingId) {

        Account account = accountRepository.findByEmail(principal.getName());
        if(account!=null)
            logger.debug("Found user");

        TrainingConfiguration tc = trainingConfigurationService.findOne(trainingId);

        if(tc.getCreator() != account) {
            tc = trainingConfigurationService.copy(tc, account);
        }
        return "configuration-editor/editor";
    }

    @RequestMapping(value = "configuration/new",method = RequestMethod.GET)
    public String createEditor(Model model)
    {
        model.addAttribute("formZone", new TrainingConfigurationForm());
        return "configuration-editor/create";
    }




}
