package edu.warbot.process.game;

import edu.warbot.game.Team;
import edu.warbot.game.WarGameSettings;
import edu.warbot.process.communication.InterProcessMessage;
import edu.warbot.process.communication.client.EndMessage;
import edu.warbot.process.communication.client.ExceptionResult;
import edu.warbot.process.communication.client.PingMessage;
import edu.warbot.process.communication.client.PreciseAgentResult;
import edu.warbot.process.communication.server.LaunchGameCommand;
import edu.warbot.process.communication.server.PreciseAgentCommand;
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

	public ClientWarbotGameAgent(WebGame wg, InputStream decoder, OutputStream encoder) throws IOException {
		super(decoder, encoder);
        this.game = wg;
	}

	public WebGame getGame(){
		return this.game;
	}


	@Override
	public void run() {
		logger.info("Starting client Warbot GameAgent");
		Thread thr = new Thread(getReader());
		Thread thr2 = new Thread(getSender());

		thr.start();
		thr2.start();

		getSender().pushMessage(new PingMessage());

		while (getAlive().get()) {
            logger.trace("isAlive client Warbot GameAgent");
            if (getReader().haveMessage()) {
                logger.info("handling message");
                InterProcessMessage ipm = getReader().pollMessage();
                handleCommand(ipm);
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		getSender().pushMessage(new EndMessage("client-thread-end"));
		getReader().shutdown();
		getSender().shutdown();
        try {
            thr.join();
            thr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Ending client Warbot GameAgent");
    }


    public void handleCommand(InterProcessMessage ipm) {
        //TODO HANDLE
        if(ipm.getHeader().equals(PingMessage.HEADER)) {
            sendMessage(new PingMessage(true));
            return;
        }

        if(ipm.getHeader().equals(PreciseAgentCommand.HEADER)) {
            //TODO HANDLE PRECISE AGENT

            return;
        }

        if(ipm.getHeader().equals(EndMessage.HEADER)) {
            getAlive().set(false);
            return;
        }
    }
}
