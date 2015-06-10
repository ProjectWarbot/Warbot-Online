package edu.warbot.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.warbot.models.Account;
import edu.warbot.models.Party;
import edu.warbot.models.TrainingAgent;
import edu.warbot.models.TrainingConfiguration;
import edu.warbot.repository.AccountRepository;
import edu.warbot.services.TrainingConfigurationService;
import edu.warbot.services.WarbotOnlineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * Created by beugnon on 21/05/15.
 */
@RestController
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class AsyncHelperController {


    private Logger logger = LoggerFactory.getLogger(AsyncHelperController.class);

    @Autowired
    private WarbotOnlineService warbotOnlineService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TrainingConfigurationService trainingConfigurationService;


    @Async
    @ResponseBody
    @RequestMapping(value = "/party/list/unmembers")
    public Map<Long, String> allUnMembers(Principal principal, @RequestParam Long idParty
            , @RequestParam(required = false) String letters) {
        Account account = accountRepository.findByEmail(principal.getName());
        Party party = warbotOnlineService.findPartyById(idParty);
        Assert.notNull(party);
        Map<Long, String> unmembers = new HashMap<>();
        if (party.getCreator().equals(account)) {

            Iterable<Account> iterable = accountRepository.findAll();
            Iterator<Account> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                Account acc = iterator.next();
                if (!party.getMembers().contains(acc) && acc.getScreenName().toLowerCase().startsWith(letters.toLowerCase()))
                    unmembers.put(acc.getId(), acc.getScreenName());
            }
        }

        return unmembers;
    }

    @Async
    @ResponseBody
    @RequestMapping(value = "configuration/update")
    public String updateTrainingConfiguration
            (Principal principal,
             @RequestParam Long trainingId,
             @RequestParam(required = false) String agents) throws IOException {
        Account account = accountRepository.findByEmail(principal.getName());
        TrainingConfiguration trainingConfiguration =
                trainingConfigurationService.findOne(trainingId);
        Set<TrainingAgent> agentsSet = new HashSet<>();
        if (agents != null) {
            ObjectMapper om = new ObjectMapper();
            try {
                List<TrainingAgent> object = om.readValue(agents, new TypeReference<List<TrainingAgent>>() {
                });
                for (TrainingAgent t : object) {
                    if (t.getTrainingConfiguration() == null)
                        t.setTrainingConfiguration(trainingConfiguration);
                }
                logger.info("list size:" + object.size());
                agentsSet.addAll(object);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        Assert.notNull(trainingConfiguration);
        if (!trainingConfiguration.getCreator().equals(account)) {
            return "unauthorized";
        }
        trainingConfigurationService
                .updateConfiguration(trainingConfiguration, agentsSet);
        return "success";
    }

    @Async
    @ResponseBody
    @RequestMapping(value = "configuration/agents")
    public List<TrainingAgent> updateTrainingConfiguration
            (Principal principal,
             @RequestParam Long trainingId) throws IOException {
        Account account = accountRepository.findByEmail(principal.getName());
        TrainingConfiguration trainingConfiguration =
                trainingConfigurationService.findOne(trainingId);
        if (!trainingConfiguration.getCreator().equals(account)) {
            return null;
        }
        List<TrainingAgent> list = new ArrayList<>();
        list.addAll(trainingConfiguration.getTrainingAgents());
        return list;
    }


}
