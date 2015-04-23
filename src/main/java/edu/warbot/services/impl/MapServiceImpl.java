package edu.warbot.services.impl;

import edu.warbot.models.Map;
import edu.warbot.repository.MapRepository;
import edu.warbot.services.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by quent on 23/04/2015.
 */
@Service
@Transactional
public class MapServiceImpl implements MapService {

    @Autowired
    private MapRepository mapRepository;
    
    public void saveMap(Map m) { mapRepository.save(m);}

    @Override
    public Map findOne(Long id) {
        return mapRepository.findOne(id);
    }

    @Override
    public Map findByName(String name) {
        return mapRepository.findByName(name);
    }

    @Override
    public Iterable<Map> findAll() {
        return mapRepository.findAll();
    }
}
