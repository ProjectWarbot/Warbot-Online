package edu.warbot.online.cluster;

import edu.warbot.online.models.Party;
import edu.warbot.online.models.TrainingConfiguration;
import edu.warbot.online.models.WebGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by beugnon on 11/06/15.
 */
public class SlaveRemoteMonoWebGameService implements RemoteWebGameService {

    private static final Integer SLOT = 1;
    private Logger logger = LoggerFactory.getLogger(SlaveRemoteMonoWebGameService.class);
    private List<WebGame> concurrentGames = Collections.synchronizedList(new ArrayList<WebGame>());


    @Override
    public Integer countMaxSlot() {
        return SLOT;
    }

    public Integer countOpenGameSlot() {
        return (countMaxSlot() - concurrentGames.size());
    }

    @Override
    public Boolean launchGame(Party p, Party p2) {
        synchronized (SlaveRemoteMonoWebGameService.class) {
            if (countOpenGameSlot() == 0) {
                logger.warn("No slot available");
                return false;
            }


            return true;
        }
    }

    @Override
    public Boolean launchGameAgainstIA(Party p, String ia) {
        return null;
    }

    @Override
    public Boolean launchGameWithTrainingRoom(Party p, Party p2, TrainingConfiguration tc) {
        return null;
    }

    @Override
    public Boolean launchGameWithTrainingRoom(Party p, String ia, TrainingConfiguration tc) {
        return null;
    }
}
