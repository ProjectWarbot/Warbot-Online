package edu.warbot.services;


import edu.warbot.models.Account;
import edu.warbot.models.TrainingConfiguration;

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
}
