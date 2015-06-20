package edu.warbot.online.services.impl;

import edu.warbot.online.models.Account;
import edu.warbot.online.models.TrainingAgent;
import edu.warbot.online.models.TrainingConfiguration;
import edu.warbot.online.repository.TrainingAgentRepository;
import edu.warbot.online.repository.TrainingConfigurationRepository;
import edu.warbot.online.services.TrainingConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by quent on 23/04/2015.
 */
@Service
@Transactional
public class TrainingConfigurationServiceImpl implements TrainingConfigurationService {

    @Autowired
    private TrainingConfigurationRepository trainingConfigurationRepository;

    @Autowired
    private TrainingAgentRepository trainingAgentRepository;

    private Logger logger = LoggerFactory.getLogger(TrainingConfigurationServiceImpl.class);

    public void save(TrainingConfiguration t) {
        trainingConfigurationRepository.save(t);
    }

    public TrainingConfiguration createTrainingConfiguration(TrainingConfiguration t) {
        trainingConfigurationRepository.save(t);
        return t;
    }

    @Override
    public void updateConfiguration(TrainingConfiguration tc, Set<TrainingAgent> agents) {

        logger.info("before remove All");
        trainingAgentRepository.deleteAll(tc.getTrainingAgents());
        logger.info("before saving");
        for (TrainingAgent t : agents) {
            logger.info("new -> " + t.isNew());
            t.resetId();
            t.setTrainingConfiguration(tc);
            trainingAgentRepository.save(t);
        }
    }

    @Override
    public List<TrainingAgent> getTrainingAgentsFrom(TrainingConfiguration tc) {
        TrainingConfiguration tc2 = trainingConfigurationRepository.findOne(tc.getId());
        return trainingAgentRepository.findByAssociatedTrainingConfiguration(tc2);
    }

    @Override
    public TrainingConfiguration findOne(Long id) {
        return trainingConfigurationRepository.findOne(id);
    }

    @Override
    public TrainingConfiguration findByName(String name) {
        return trainingConfigurationRepository.findByName(name);
    }

    @Override
    public Iterable<TrainingConfiguration> findAll() {
        return trainingConfigurationRepository.findAll();
    }


    @Override
    public TrainingConfiguration copy(TrainingConfiguration tc, Account newCreator) {
        //Ici le nom de la map est copiée
        String newName = newCreator.getScreenName() + tc.getName() + tc.getId();
        TrainingConfiguration tmp = new TrainingConfiguration(newName, tc.getLevel(), tc.getDescription());
        tmp.setCreator(newCreator);
        trainingConfigurationRepository.save(tmp);
        return tmp;
    }


}
