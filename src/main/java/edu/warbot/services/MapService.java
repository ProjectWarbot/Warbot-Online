package edu.warbot.services;

import edu.warbot.models.Map;

/**
 * Created by quent on 23/04/2015.
 */
public interface MapService {

    void saveMap(Map map);

    Map findOne(Long id);

    Map findByName(String name);

    Iterable<Map> findAll();
}
