package edu.warbot.process.game;

import edu.warbot.process.communication.InterProcessMessage;
import edu.warbot.process.communication.WarbotProcessReader;
import edu.warbot.process.communication.WarbotProcessSender;
import edu.warbot.process.communication.client.EndMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by beugnon on 23/04/15.
 *
 */
public abstract class WarbotGameAgent implements Runnable {

    private WarbotProcessReader reader;

    private WarbotProcessSender sender;

    private AtomicBoolean alive;


    public WarbotGameAgent(InputStream decoder, OutputStream encoder) throws IOException {

        alive = new AtomicBoolean(true);
        reader = new WarbotProcessReader(decoder);
        sender = new WarbotProcessSender(encoder);
    }

    public void sendMessage(InterProcessMessage ipm) {
        sender.pushMessage(ipm);
    }

    public WarbotProcessReader getReader() {
        return reader;
    }

    public WarbotProcessSender getSender() {
        return sender;
    }

    public AtomicBoolean getAlive() {
        return alive;
    }
}
