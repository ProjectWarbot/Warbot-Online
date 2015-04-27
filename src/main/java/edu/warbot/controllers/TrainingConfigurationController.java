package edu.warbot.controllers;

import edu.warbot.form.TrainingConfigurationForm;
import edu.warbot.models.TrainingConfiguration;
import edu.warbot.services.TrainingConfigurationService;
import edu.warbot.services.WarbotOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jimmy on 13/04/15.
 */

@Controller
@Secured({"ROLE_USER","ROLE_ADMIN"})
public class TrainingConfigurationController {

    final Logger logger = LoggerFactory.getLogger(TrainingConfigurationController.class);

    @Autowired
    private TrainingConfigurationService trainingConfigurationService;

    @RequestMapping(value = "configuration/list", method = RequestMethod.GET)
    public String listEditor(Model model) {
        Iterable<TrainingConfiguration> trainingConfigurationsList;
        trainingConfigurationsList = trainingConfigurationService.findAll();
        model.addAttribute("trainingConfigurations", trainingConfigurationsList);
        return "configuration-editor/select";
    }

    @RequestMapping(value = "configuration/create", method = RequestMethod.POST)
    public String createEditor() {

        return "configuration-editor/editor";
    }

    @RequestMapping(value = "configuration/new",method = RequestMethod.GET)
    public String createEditor(Model model)
    {
        model.addAttribute("formZone", new TrainingConfigurationForm());
        return "configuration-editor/create";
    }




}
