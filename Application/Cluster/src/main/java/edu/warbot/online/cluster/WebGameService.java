package edu.warbot.online.cluster;

import edu.warbot.online.models.Party;
import edu.warbot.online.models.TrainingConfiguration;

/**
 * Created by beugnon on 11/06/15.
 */
public interface WebGameService {


    Boolean launchGame(Party p, Party p2);

    Boolean launchGameAgainstIA(Party p, String ia);

    Boolean launchGameWithTrainingRoom(Party p, Party p2, TrainingConfiguration tc);

    Boolean launchGameWithTrainingRoom(Party p, String ia, TrainingConfiguration tc);

}
