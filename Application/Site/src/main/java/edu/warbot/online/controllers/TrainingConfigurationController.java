package edu.warbot.online.controllers;

import edu.warbot.online.form.TrainingConfigurationForm;
import edu.warbot.online.models.Account;
import edu.warbot.online.models.TrainingConfiguration;
import edu.warbot.online.repository.AccountRepository;
import edu.warbot.online.services.TrainingConfigurationService;
import edu.warbot.online.support.web.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Created by jimmy on 13/04/15.
 */

@Controller
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class TrainingConfigurationController {

    final Logger logger = LoggerFactory.getLogger(TrainingConfigurationController.class);

    @Autowired
    private TrainingConfigurationService trainingConfigurationService;

    @Autowired
    private AccountRepository accountRepository;


    @RequestMapping(value = "configuration/list", method = RequestMethod.GET)
    public String listEditor(Principal principal, Model model) {
        Account account = accountRepository.findByEmail(principal.getName());
        Iterable<TrainingConfiguration> trainingConfigurationsList;
        trainingConfigurationsList = trainingConfigurationService.findAll();
        model.addAttribute("trainingConfigurations", trainingConfigurationsList);
        model.addAttribute(account);
        return "configuration-editor/select";
    }

    @RequestMapping(value = "configuration/new", method = RequestMethod.GET)
    public String createEditor(Model model) {
        model.addAttribute("formZone", new TrainingConfigurationForm());
        return "configuration-editor/create";
    }

    @RequestMapping(value = "configuration/create", method = RequestMethod.POST)
    public String createEditor(Principal principal, @Valid @ModelAttribute("form")
    TrainingConfigurationForm tcForm,
                               RedirectAttributes ra) {

        Account account = accountRepository.findByEmail(principal.getName());
        if (account != null)
            logger.debug("Found user");

        TrainingConfiguration tc = tcForm.createTestZone();
        tc.setCreator(account);
        tc = trainingConfigurationService.createTrainingConfiguration(tc);

        ra.addAttribute("trainingId", tc.getId());
        return "redirect:/configuration/edit";
    }

    @RequestMapping(value = "configuration/edit", method = RequestMethod.GET)
    public String edit(Principal principal, @RequestParam Long trainingId, Model model, RedirectAttributes ra) {
        Account account = accountRepository.findByEmail(principal.getName());
        Assert.notNull(account);
        TrainingConfiguration tc;
        tc = trainingConfigurationService.findOne(trainingId);
        Assert.notNull(tc);
        if (!tc.getCreator().getId().equals(account.getId())) {
            MessageHelper.addErrorAttribute(ra, "training.configuration.fail");
            return "redirect:/configuration/list";
        }
        model.addAttribute("tc", tc);
        return "configuration-editor/editor";
    }

}
