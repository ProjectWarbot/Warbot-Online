package edu.warbot.process.game;

import edu.warbot.game.Team;
import edu.warbot.game.WarGameSettings;
import edu.warbot.process.communication.InterProcessMessage;
import edu.warbot.process.communication.client.EndMessage;
import edu.warbot.process.communication.client.ExceptionResult;
import edu.warbot.process.communication.client.PingMessage;
import edu.warbot.process.communication.server.LaunchGameCommand;
import edu.warbot.services.impl.TeamServiceImpl;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by beugnon on 29/04/15.
 *
 * Traitement des messages de la communication inter-processus coté client
 *
 * @author beugnon
 *
 *
 */
public class ClientWarbotGameAgent extends WarbotGameAgent {

	private Logger logger = LoggerFactory.getLogger(ClientWarbotGameAgent.class);

	private WebGame game;

	public ClientWarbotGameAgent(InputStream decoder, OutputStream encoder) throws IOException {
		super(decoder, encoder);
	}

	public WebGame getGame(){
		return this.game;
	}

	@Override
	public void run() {
		System.err.println("Starting to run");
		Thread thr = new Thread(getReader());
		Thread thr2 = new Thread(getSender());

		thr.start();
		thr2.start();

		getSender().pushMessage(new PingMessage());

		while (getAlive().get()) {
			if (getReader().haveMessage()) {
				System.err.println("have message");
				InterProcessMessage ipm = getReader().pollMessage();
				System.err.println("ipm");
				if(game != null) {
					if(ipm.getHeader().equals(LaunchGameCommand.HEADER)) {
						//LAUNCH A GAME
						LaunchGameCommand lgc = (LaunchGameCommand) ipm;
						WarGameSettings wgs = new WarGameSettings();
						TeamServiceImpl tsi = new TeamServiceImpl();
						tsi.init();
						Team ts = tsi.getIATeamByName("Engineer");
						Team ts2 = ts.duplicate(ts.getName()+"_bis");


//                        Team t1;
//                        if(lgc.getCrossProcessTeam1().getLanguage()==null) {
//                            t1 = new Team(lgc.getCrossProcessTeam1().getTeamName());
//                            for(String agent : lgc.getCrossProcessTeam1()
//                                    .getBrainByAgent().keySet())
//                                t1.addBrainControllerClassForAgent(agent,
//                                        lgc.getCrossProcessTeam1()
//                                                .getBrainByAgent().get(agent));
//                        }
//                        else {
//                            ScriptedTeam st1 = new ScriptedTeam(lgc.getCrossProcessTeam1()
//                                    .getTeamName(),
//                                    lgc.getCrossProcessTeam1().getBrainByAgent());
//                            st1.setInterpreter(ScriptInterpreterFactory
//                                    .getInstance(lgc.getCrossProcessTeam1().getLanguage())
//                                    .createScriptInterpreter());
//                            t1 = st1;
//                        }
						wgs.addSelectedTeam(ts);
//
//                        Team t2;
//                        if(lgc.getCrossProcessTeam2().getLanguage()==null) {
//                            t2 = new Team(lgc.getCrossProcessTeam2().getTeamName());
//                            for(String agent : lgc.getCrossProcessTeam2()
//                                    .getBrainByAgent().keySet())
//                                t2.addBrainControllerClassForAgent(agent,
//                                        lgc.getCrossProcessTeam2()
//                                                .getBrainByAgent().get(agent));
//                        }
//                        else {
//                            ScriptedTeam st2 = new ScriptedTeam(lgc.getCrossProcessTeam2()
//                                    .getTeamName(),
//                                    lgc.getCrossProcessTeam2().getBrainByAgent());
//                            st2.setInterpreter(ScriptInterpreterFactory
//                                    .getInstance(lgc.getCrossProcessTeam2().getLanguage())
//                                    .createScriptInterpreter());
//                            t2 = st2;
//                        }
						wgs.addSelectedTeam(ts2);
//
                        this.game = new WebGame(this,wgs);
						WebLauncher wl = new WebLauncher(getGame());
						Madkit madkit = new Madkit(Madkit.BooleanOption.desktop.toString(),"true");
						madkit.doAction(KernelAction.LAUNCH_AGENT,wl);
					}
					else {
						//RECEIVE COMMAND WITHOUT INITIALIZED GAME
						logger.error("UNSUCESSFUL GAME START");
						getSender().pushMessage(new ExceptionResult(new Exception("Error")));
						System.exit(0);
					}
				}
                handleCommand(ipm);
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		getSender().pushMessage(new EndMessage("thread-end"));
		getReader().shutdown();
		getSender().shutdown();
	}

    public void handleCommand(InterProcessMessage ipm) {
        //TODO HANDLE
    }
}
