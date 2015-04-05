package edu.warbot.online;

import edu.warbot.game.WarGameSettings;
import edu.warbot.launcher.WarLauncher;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import turtlekit.kernel.TurtleKit;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by beugnon on 05/04/15.
 */
public class MadkitTest {

    public static void main(String[] args)
    {
        String[] madkitArgs = new String[]
                {Madkit.BooleanOption.desktop.toString(), "false",
                };
        Madkit m = new Madkit(madkitArgs);
        System.out.printf("here");
        WebGame game = new WebGame("toto",null,new WarGameSettings());
        WebLauncher wl = new WebLauncher(game);
        System.out.printf("here");
        m.doAction(KernelAction.LAUNCH_AGENT, wl);
        System.out.printf("here");
    }
}
