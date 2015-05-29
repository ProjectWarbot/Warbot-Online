package edu.warbot.services.impl;

import edu.warbot.models.Account;
import edu.warbot.models.TrainingConfiguration;
import edu.warbot.repository.TrainingConfigurationRepository;
import edu.warbot.services.TrainingConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by quent on 23/04/2015.
 */
@Service
@Transactional
public class TrainingConfigurationServiceImpl implements TrainingConfigurationService {

    @Autowired
    private TrainingConfigurationRepository trainingConfigurationRepository;

    public void save(TrainingConfiguration t) {
        trainingConfigurationRepository.save(t);
    }

    public TrainingConfiguration createTrainingConfiguration(TrainingConfiguration t) {
        trainingConfigurationRepository.save(t);
        return t;
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
        //Ici le nom de la map copiée est
        String newName = newCreator.getScreenName() + tc.getName() + tc.getId();
        TrainingConfiguration tmp = new TrainingConfiguration(newName, tc.getLevel(), tc.getDescription());
        tmp.setCreator(newCreator);
        trainingConfigurationRepository.save(tmp);
        return tmp;
    }


}
