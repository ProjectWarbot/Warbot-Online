package edu.warbot.online;

import edu.warbot.game.WarGameListener;
import edu.warbot.launcher.WarScheduler;
import madkit.action.KernelAction;
import madkit.message.KernelMessage;

/**
 * Created by beugnon on 06/04/15.
 */
public class WebScheduler extends WarScheduler implements WarGameListener {

    public WebScheduler(WebGame game)
    {
        super();
        setGame(game);

    }

    @Override
    public void onGameOver() {
        super.onGameOver();
        sendMessage("local", "system", "manager", new KernelMessage(KernelAction.EXIT, new Object[0]));
    }
}

