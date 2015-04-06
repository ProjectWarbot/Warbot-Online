package edu.warbot.online;

import edu.warbot.game.WarGameListener;
import edu.warbot.launcher.WarScheduler;

/**
 * Created by beugnon on 06/04/15.
 */
public class WebScheduler extends WarScheduler implements WarGameListener {

    public WebScheduler(WebGame game)
    {
        super();
        setGame(game);
    }
}

