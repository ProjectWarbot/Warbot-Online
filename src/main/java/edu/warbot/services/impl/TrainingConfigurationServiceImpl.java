package edu.warbot.services.impl;

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
    private TrainingConfigurationRepository mapRepository;

    public void saveMap(TrainingConfiguration m) { mapRepository.save(m);}

    @Override
    public TrainingConfiguration findOne(Long id) {
        return mapRepository.findOne(id);
    }

    @Override
    public TrainingConfiguration findByName(String name) {
        return mapRepository.findByName(name);
    }

    @Override
    public Iterable<TrainingConfiguration> findAll() {
        return mapRepository.findAll();
    }
}
