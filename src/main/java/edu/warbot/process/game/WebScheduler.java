package edu.warbot.process.game;

import edu.warbot.game.WarGameListener;
import edu.warbot.launcher.WarScheduler;
import madkit.action.KernelAction;
import madkit.action.SchedulingAction;
import madkit.message.KernelMessage;
import madkit.message.SchedulingMessage;
import turtlekit.agr.TKOrganization;

/**
 * Created by beugnon on 06/04/15.
 */
public class WebScheduler extends WarScheduler implements WarGameListener {

    public WebScheduler(WebGame game) {
        super();
        setGame(game);

    }

    public void onGameStopped() {
        super.onGameStopped();
        sendMessage(TKOrganization.TK_COMMUNITY, TKOrganization.ENGINE_GROUP, TKOrganization.SCHEDULER_ROLE,
                new SchedulingMessage(SchedulingAction.PAUSE));
    }

    @Override
    public void onGameOver() {
        super.onGameOver();
        sendMessage("local", "system", "manager", new KernelMessage(KernelAction.EXIT));
    }
}

