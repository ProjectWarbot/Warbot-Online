package edu.warbot.process.game;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by beugnon on 23/04/15.
 */
public class MainWarbot {
    public static class DocumentOutputStream extends OutputStream {

        private Document doc;

        public DocumentOutputStream(Document doc) {
            this.doc = doc;
        }

        public void write(int b) throws IOException {
            write(new byte[]{(byte)b}, 0, 1);
        }

        public void write(byte b[], int off, int len) throws IOException {
            try {
                doc.insertString(doc.getLength(),
                        new String(b, off, len), null);
                if(doc.getLength()>10000)
                    doc.remove(0,10000);
            }
            catch (BadLocationException ble) {
                throw new IOException(ble.getMessage());
            }
        }
    }

    protected static PrintStream os;

    static {
        os = System.out;
        JFrame jf = new JFrame("");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setPreferredSize(new Dimension(800,600));
        JTextArea jta = new JTextArea();
        jta.setEditable(false);
        jf.getContentPane().add(jta);
        jf.pack();
        jf.setVisible(true);
        OutputStream dos = new DocumentOutputStream(jta.getDocument());
        System.setOut(new PrintStream(dos));
        System.setErr(new PrintStream(dos));
    }

    public static void main(String[] args) {

        try {
            ClientWarbotGameAgent cwga = new ClientWarbotGameAgent(System.in,os);
            Thread thread = new Thread(cwga);
            thread.start();
            thread.join();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
