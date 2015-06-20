package edu.warbot.online.cluster;

/**
 * Created by beugnon on 11/06/15.
 */
public interface RemoteWebGameService extends WebGameService {

    Integer countOpenGameSlot();

    Integer countMaxSlot();

}
