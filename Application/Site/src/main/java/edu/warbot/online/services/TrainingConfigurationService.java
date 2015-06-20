package edu.warbot.online.services;


import edu.warbot.online.models.Account;
import edu.warbot.online.models.TrainingAgent;
import edu.warbot.online.models.TrainingConfiguration;

import java.util.List;
import java.util.Set;

/**
 * Created by quent on 23/04/2015.
 */
public interface TrainingConfigurationService {

    void save(TrainingConfiguration map);

    TrainingConfiguration findOne(Long id);

    TrainingConfiguration findByName(String name);

    Iterable<TrainingConfiguration> findAll();

    TrainingConfiguration copy(TrainingConfiguration tc, Account newCreator);

    TrainingConfiguration createTrainingConfiguration(TrainingConfiguration t);

    void updateConfiguration(TrainingConfiguration tc, Set<TrainingAgent> agents);

    List<TrainingAgent> getTrainingAgentsFrom(TrainingConfiguration tc);
}
