package edu.warbot.process.game;


import edu.warbot.models.Account;
import edu.warbot.process.communication.InterProcessMessage;
import edu.warbot.process.communication.client.AgentMessage;
import edu.warbot.process.communication.client.EndMessage;
import edu.warbot.process.communication.client.ExceptionResult;
import edu.warbot.process.communication.client.PingMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

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


    private final Process process;

    private final Logger logger = LoggerFactory.getLogger(ServerWarbotGameAgent.class);

    private Account account;

    private SimpMessageSendingOperations messagingTemplate;

    private Queue<InterProcessMessage> ipc;

    private boolean activated = false;

    private int cpt=0;

    public ServerWarbotGameAgent(Account account, Process p, InputStream decoder, OutputStream encoder, SimpMessageSendingOperations simp) throws IOException {
        super(decoder, encoder);
        this.process = p;
        this.account = account;
        this.messagingTemplate = simp;
        this.ipc = new ArrayDeque<>();
    }

    public void pushMessage(InterProcessMessage ipm) {
        if(activated)
            getSender().pushMessage(ipm);
        else
            this.ipc.add(ipm);
    }

    public void stop() {
        process.destroy();
        getAlive().set(false);
    }

    @Override
    public void run() {
        Thread thr = new Thread(getReader());
        Thread thr2 = new Thread(getSender());

        Thread processNotifier = new Thread(new Runnable() {
            @Override
            public void run() {
                System.err.println("starting to listen process");
                int i = 0;
                try {
                    i = process.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println("ending waitFor Process return : "+i);
                getAlive().set(false);
            }
        });
        thr.start();
        thr2.start();
        processNotifier.start();
        while (getAlive().get()) {
            if (getReader().haveMessage()) {
                cpt=0;
                InterProcessMessage ipm = getReader().pollMessage();
                if(activated)
                    handleMessage(ipm);
                else if(ipm.getHeader().equals(PingMessage.HEADER)) {
                    activated=true;
                    getSender().pushMessage(new PingMessage(true));
                    while(!ipc.isEmpty())
                        getSender().pushMessage(ipc.poll());
                }
                else {
                    //Received message without be activated
                    cpt++;
                    if(cpt== 300) {
                        getSender().pushMessage(new PingMessage());
                    } else if (cpt >= 30000) {
                        getSender().pushMessage(new EndMessage("timeout"));
                        handleMessage(new EndMessage("timeout"));
                    }
                }

            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        getSender().pushMessage(new EndMessage("server-thread-end"));
        getReader().shutdown();
        getSender().shutdown();

        try {
            thr.join();
            thr2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handleMessage(InterProcessMessage ipm) {
        if(ipm.getHeader().equals(EndMessage.HEADER)) {
            logger.info( ((EndMessage) ipm).getContent().toString());
            getAlive().set(false);
        }

        if(ipm.getHeader().equals(ExceptionResult.HEADER)) {
            logger.info( ((ExceptionResult) ipm).getException().getMessage());

            getAlive().set(false);
        }

        if (!ipm.getHeader().equals(AgentMessage.HEADER))
            messagingTemplate.convertAndSendToUser(account.getEmail(), "/queue/game", ipm);
        else {
            AgentMessage a = (AgentMessage) ipm;
            messagingTemplate.convertAndSendToUser
                    (account.getEmail(), "/queue/game.agents."
                            + a.getContent().get("name"), a);
        }
    }
}
