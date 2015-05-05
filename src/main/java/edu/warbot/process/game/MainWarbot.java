package edu.warbot.process.game;

import edu.warbot.config.DataSourceConfig;
import edu.warbot.config.WarbotProcessConfig;
import edu.warbot.game.Team;
import edu.warbot.game.WarGame;
import edu.warbot.game.WarGameSettings;
import edu.warbot.launcher.WarLauncher;
import edu.warbot.launcher.WarMain;
import edu.warbot.models.Party;
import edu.warbot.process.communication.InterProcessMessage;
import edu.warbot.process.communication.JSONInterProcessMessageTranslater;
import edu.warbot.process.communication.client.EndMessage;
import edu.warbot.process.communication.client.ExceptionResult;
import edu.warbot.process.communication.client.PingMessage;
import edu.warbot.process.communication.server.LaunchGameCommand;
import edu.warbot.process.exception.UnrecognizedInterProcessMessageException;
import edu.warbot.repository.PartyRepository;
import edu.warbot.services.TeamService;
import madkit.action.KernelAction;
import madkit.kernel.Madkit;
import madkit.kernel.MadkitOption;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

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

    /**Writes to nowhere*/
    public static class NullOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
        }
    }

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MainWarbot.class);


    protected static PrintStream os;

    static {
        os = System.out;
        JFrame jf = new JFrame("");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setPreferredSize(new Dimension(800,600));
        JTextArea jta = new JTextArea();
        jta.setEditable(true);
        JScrollPane jsp = new JScrollPane();
        jsp.createVerticalScrollBar();
        jsp.setViewportView(jta);

        jf.getContentPane().add(jsp);
        jf.pack();
        jf.setVisible(true);
        OutputStream dos = new DocumentOutputStream(jta.getDocument());
        System.setOut(new PrintStream(new NullOutputStream()));
        System.setErr(new PrintStream(dos));
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext acac = new AnnotationConfigApplicationContext(WarbotProcessConfig.class,DataSourceConfig.class);
        if(args.length>0) {
            logger.info("Arguments ->");
            for(String s : args)
                logger.info(s);
        }
        else {
            logger.error("No Argument !");
            os.println(new ExceptionResult(new Exception("NoArguement")));
            System.exit(1);
        }

        if(JSONInterProcessMessageTranslater.isReadableJSON(args[0])) {
            try {
                InterProcessMessage ipm = JSONInterProcessMessageTranslater.convertIntoObject(args[0]);
                if(!ipm.getHeader().equals(LaunchGameCommand.HEADER)) {
                    os.println(JSONInterProcessMessageTranslater.convertIntoMessage(new ExceptionResult(new Exception("NotLaunchGame"))));
                }
                else {
                    WebGame wg = prepareWebGame(ipm,System.in,os, acac);
                    wg.sendMessage(new PingMessage());
                    WebLauncher wl = new WebLauncher(wg);
                    Madkit madkit = new Madkit(Madkit.BooleanOption.desktop.toString(),"false");
                    madkit.doAction(KernelAction.LAUNCH_AGENT,wl);
                    wg.getWarbotAgent().join();
                }
            } catch (UnrecognizedInterProcessMessageException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        else {
            logger.error("here unreadable");
            os.println(new ExceptionResult(new Exception("UnreadableArgument")));
            System.exit(2);
        }
        System.exit(0);
    }

    private static Team loadTeam(TeamService ts, PartyRepository pr,String teamname, boolean isIATeam) {
        Team t1=null;
        if(isIATeam) {
            if(!LaunchGameCommand.IA_TEAM_RANDOM.equals(teamname)) {
                String name = teamname.substring(LaunchGameCommand.IA_TEAM_HEADER.length());
                t1 = ts.getIATeamByName(name);
            }
            else
                t1 = ts.getRandomIATeam();
        }
        else {
            Long id = Long.parseLong(teamname.
                    substring(LaunchGameCommand.PLAYER_TEAM_HEADER.length()));
            Party party = pr.findOne(id);
            if(party==null) {
                System.out.println("NOT FOUND ID:"+id);
            }
            else
                t1 = ts.generateTeamFromParty(party);
        }
        return t1;
    }

    private static WebGame prepareWebGame(InterProcessMessage ipm,InputStream is, OutputStream os, AnnotationConfigApplicationContext acac) throws IOException {
        LaunchGameCommand lgc = (LaunchGameCommand) ipm;
        TeamService ts = (TeamService) acac.getBean("teamService");
        WarGameSettings wgs = new WarGameSettings();
        PartyRepository pr = (PartyRepository) acac.getBean("partyRepository");

        System.out.println("before loading teams");
        Team t1 = loadTeam(ts, pr, lgc.getTeam1(), lgc.isIATeam1());
//        Assert.notNull(t1);
        Team t2 = loadTeam(ts, pr, lgc.getTeam2(), lgc.isIATeam2());
//        Assert.notNull(t2);
        if (t1 == null || t2 == null) {
            System.out.println(" T1 IS " + ((t1 == null) ? "NULL" : "NOT NULL"));
            System.out.println(" T2 IS " + ((t2 == null) ? "NULL" : "NOT NULL"));
            throw new IOException();
        } else {
            wgs.addSelectedTeam(t1);
            wgs.addSelectedTeam(t2);
            WebGame wg = new WebGame(is, MainWarbot.os, wgs);
            return wg;
        }

    }
}
