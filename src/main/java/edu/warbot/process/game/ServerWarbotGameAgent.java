package edu.warbot.process.game;


import edu.warbot.models.Account;
import edu.warbot.process.communication.InterProcessMessage;
import edu.warbot.process.communication.client.AgentMessage;
import edu.warbot.process.communication.client.EndMessage;
import edu.warbot.process.communication.client.PingMessage;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by beugnon on 29/04/15.
 *
 * Traitement des messages de la communication inter-processus côté serveur
 *
 * @author beugnon
 *
 *
 */
public class ServerWarbotGameAgent extends WarbotGameAgent {


    private Account account;

    private SimpMessageSendingOperations messagingTemplate;

    public ServerWarbotGameAgent(Account account, InputStream decoder, OutputStream encoder, SimpMessageSendingOperations simp) throws IOException, IOException {
        super(decoder, encoder);
        this.account = account;
        this.messagingTemplate = simp;
    }

    @Override
    public void run() {
        Thread thr = new Thread(getReader());
        Thread thr2 = new Thread(getSender());

        thr.start();
        thr2.start();
        while (getAlive().get()) {
            if (getReader().haveMessage()) {
                InterProcessMessage ipm = getReader().pollMessage();
                if (!ipm.getHeader().equals(AgentMessage.HEADER))
                    messagingTemplate.convertAndSendToUser(account.getEmail(), "/queue/game", ipm);
                else if(ipm.getHeader().equals(PingMessage.HEADER))
                    System.err.println("PING");
                else {
                    AgentMessage a = (AgentMessage) ipm;
                    messagingTemplate.convertAndSendToUser
                            (account.getEmail(), "/queue/game.agents."
                                    + a.getContent().get("name"), a);
                }
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
}
