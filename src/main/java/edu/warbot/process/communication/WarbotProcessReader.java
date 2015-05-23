package edu.warbot.process.communication;

import edu.warbot.process.exception.UnrecognizedInterProcessMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by beugnon on 21/04/15.
 *
 */
public class WarbotProcessReader implements Runnable {


    private Logger logger = LoggerFactory.getLogger(WarbotProcessReader.class);

    private AtomicBoolean alive;

    private InputStream inputStream;

    private ConcurrentLinkedQueue<InterProcessMessage> commands;

    public WarbotProcessReader(InputStream inputStream) throws IOException {
        logger.trace("<init>");
        this.inputStream = inputStream;
        this.alive = new AtomicBoolean(true);
        this.commands = new ConcurrentLinkedQueue<>();
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void shutdown() {
        this.alive.set(false);
    }

    public boolean isAlive() {
        return this.alive.get();
    }

    private void pushMessage(InterProcessMessage o) {
        commands.add(o);
    }

    public InterProcessMessage pollMessage() {
        return commands.poll();
    }

    public boolean haveMessage() {
        return !commands.isEmpty();
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(getInputStream());
        while (isAlive()) {
            while(sc.hasNextLine()) {
                try {
                    String s = sc.nextLine();
                    logger.debug("[INFO] Received : "+s);
                    if(JSONInterProcessMessageTranslater.isReadableJSON(s)) {
                        InterProcessMessage ipm = JSONInterProcessMessageTranslater.convertIntoObject(s);
                        pushMessage(ipm);
                    }
                } catch (UnrecognizedInterProcessMessageException e) {
                    e.printStackTrace();
                    logger.error("Unrecognized message to push ! ", e);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Can't convert message in IPM", e);
                }
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sc.close();
        logger.info("END of WarbotProcessReader");
        this.alive.set(false);
    }
}
