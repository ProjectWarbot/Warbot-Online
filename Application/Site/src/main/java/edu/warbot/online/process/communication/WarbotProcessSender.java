package edu.warbot.online.process.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by beugnon on 21/04/15.
 */
public class WarbotProcessSender<S extends InterProcessMessage> implements Runnable {

    private AtomicBoolean alive;

    private PrintStream objectOutputStream;

    private ConcurrentLinkedQueue<S> results;

    private Logger logger = LoggerFactory.getLogger(WarbotProcessSender.class);

    public WarbotProcessSender(OutputStream objectOutputStream) throws IOException {
        this.objectOutputStream = new PrintStream(objectOutputStream);
        this.alive = new AtomicBoolean(true);
        this.results = new ConcurrentLinkedQueue<S>();
    }

    public PrintStream getOutputStream() {
        return objectOutputStream;
    }

    public void shutdown() {
        logger.info("Was shutdowned !");
        this.alive.set(false);
    }

    public boolean isAlive() {
        return this.alive.get();
    }

    public void pushMessage(S o) {
        results.add(o);
    }

    private S pollMessage() {
        return results.poll();
    }

    @Override
    public void run() {
        while (isAlive()) {
            while (!results.isEmpty()) {
                S pr = pollMessage();
                try {
                    logger.debug("prepare to send: " + pr.getHeader());
                    String s = JSONInterProcessMessageTranslater.
                            convertIntoMessage(pr);
                    getOutputStream().println(s);
                    logger.debug("Sent data: " + s);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Can't send message with header" + pr.getHeader(), e);
                }

            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.alive.set(false);
        logger.warn("Kill Warbot Sender ");
        while (!results.isEmpty()) {
            S pr = pollMessage();
            try {
                logger.info("prepare to send: " + pr.getHeader());
                String s = JSONInterProcessMessageTranslater.
                        convertIntoMessage(pr);
                getOutputStream().println(s);
                logger.info("Sent data: " + s);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Can't send message with header" + pr.getHeader(), e);
            }

        }
    }
}
