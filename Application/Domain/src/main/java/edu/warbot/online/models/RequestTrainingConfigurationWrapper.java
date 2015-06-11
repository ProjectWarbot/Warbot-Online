package edu.warbot.online.models;

import java.util.Set;

/**
 * Created by quent on 05/05/2015.
 */
public class RequestTrainingConfigurationWrapper {

    private Set<TrainingAgent> trainingAgents;

    private TrainingConfiguration map;


    public TrainingConfiguration getMap() {
        return map;
    }

    public void setMap(TrainingConfiguration map) {
        this.map = map;
    }

    public Set<TrainingAgent> getTrainingAgents() {
        return trainingAgents;
    }

    public void setTrainingAgents(Set<TrainingAgent> trainingAgents) {
        this.trainingAgents = trainingAgents;
    }
}
