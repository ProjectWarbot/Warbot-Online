package edu.warbot.online.services.impl;

import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.teams.JavaTeam;
import edu.warbot.agents.teams.ScriptedTeam;
import edu.warbot.agents.teams.Team;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.capacities.Agressive;
import edu.warbot.brains.implementations.AgentBrainImplementer;
import edu.warbot.brains.implementations.WarBrainImplementation;
import edu.warbot.launcher.TeamConfigReader;
import edu.warbot.launcher.UserPreferences;
import edu.warbot.online.models.Party;
import edu.warbot.online.models.WebCode;
import edu.warbot.online.repository.PartyRepository;
import edu.warbot.online.repository.WebCodeRepository;
import edu.warbot.online.services.TeamService;
import edu.warbot.scriptcore.interpreter.ScriptInterpreterFactory;
import edu.warbot.tools.WarIOTools;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teams.engineer.WarExplorerBrainController;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by beugnon on 05/04/15.
 *
 * @author beugnon
 */
@Service
@Transactional
public class TeamServiceImpl implements TeamService {
    private static final Logger logger = LoggerFactory.getLogger
            (TeamServiceImpl.class);
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private WebCodeRepository webCodeRepository;
    private Map<String, Team> sourcesTeam = new HashMap<>();


    public void loadSourceTeams() {
        Map<String, String> teamsSourcesFolders = UserPreferences.getTeamsSourcesFolders();
        for (String currentFolder : teamsSourcesFolders.values()) {
            logger.info("Introspection in " + currentFolder);
            try {

                InputStream input = WarExplorerBrainController.class.getClassLoader().getResourceAsStream("" + currentFolder + "/" + TeamConfigReader.FILE_NAME);
                if (input != null) {
                    logger.info("Introspection starting in " + currentFolder);

                    TeamConfigReader teamConfigReader = new TeamConfigReader();
                    teamConfigReader.load(input);
                    input.close();
                    Team e = this.loadTeamFromSources(teamsSourcesFolders, teamConfigReader);
                    System.out.println((e == null) ? "is NULL" : null);
                    System.out.println(e.getTeamName());
                    if (getTeams().containsKey(e.getTeamName()))
                        logger.error("Erreur lors de la lecture d\'une équipe : le nom " + e.getTeamName() +
                                " est déjà utilisé.");
                    else {
                        logger.info("Source team added", e.getTeamName());
                        getTeams().put(e.getTeamName(), e);
                    }
                    input.close();
                }


            } catch (FileNotFoundException var8) {
                logger.error(
                        "Le fichier de configuration est introuvable " +
                                "dans le dossier " +
                                (new File("")).getAbsolutePath() +
                                File.separatorChar + currentFolder, var8);
            } catch (MalformedURLException var9) {
                logger.error("Lecture des fichiers JAR : URL mal formée", var9);
            } catch (ClassNotFoundException var10) {
                logger.error("Lecture des fichiers JAR : Classe non trouvée");
                var10.printStackTrace();
            } catch (IOException var11) {
                logger.error("Lecture des fichiers JAR : Lecture de fichier");
                var11.printStackTrace();
            } catch (NullPointerException var12) {
                logger.error("Lecture des fichiers JAR : Un pointeur nul est apparu");
                var12.printStackTrace();
            } catch (CannotCompileException var13) {
                logger.error("Lecture des fichiers JAR : Impossible de compiler une classe", var13);
            } catch (NotFoundException var14) {
                logger.error("Lecture des fichiers JAR : Impossible de trouver la classe", var14);
            }
        }
    }


    public Team generateTeamFromParty(Party party) {

        ScriptedTeam team = new ScriptedTeam(party.getName(), "", null);
        team.setInterpreter
                (ScriptInterpreterFactory.getInstance(party.getLanguage())
                        .createScriptInterpreter());

        for (WebCode webcode : webCodeRepository.findWebCodeByParty(party)) {
            StringBuilder sb = new StringBuilder(webcode.getContent());
            team.getInterpreter().addScript(sb, webcode.getAgent().getType());
        }
        return team;
    }

    @Override
    public Team getIATeamByName(String name) {
        if (getTeams().size() == 0)
            loadSourceTeams();
        return getTeams().get(name);
    }

    @Override
    public Team getRandomIATeam() {
        if (getTeams().isEmpty())
            loadSourceTeams();

        Random r = new Random();
        int cpt = r.nextInt(getTeams().size());
        Iterator<Team> it = getTeams().values().iterator();
        Team t1 = it.next();
        while (cpt != 0 && it.hasNext())
            t1 = it.next();
        return t1;
    }

    private Map<String, Team> getTeams() {
        return sourcesTeam;
    }


    protected Team loadTeamFromSources(Map<String, String> teamsSourcesFolders, final TeamConfigReader teamConfigReader) throws ClassNotFoundException, IOException, NotFoundException, CannotCompileException {

        URL teamDirectoryURL = WarExplorerBrainController.class.getClassLoader().getResource("" + teamsSourcesFolders.get(teamConfigReader.getTeamName()));
        if (teamDirectoryURL != null) {
            File teamDirectory = new File(teamDirectoryURL.getFile());


            File l[] = teamDirectory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.equalsIgnoreCase(teamConfigReader.getIconPath());
                }
            });

            Map<String, String> brainControllersClassesName = teamConfigReader.
                    getBrainControllersClassesNameOfEachAgentType();

            Map<WarAgentType, Class<? extends WarBrain>> brains = new HashMap<>();
            if (!teamConfigReader.isFSMTeam()) {
                ClassPool defaultClassPool = ClassPool.getDefault();
                for (String agentName : brainControllersClassesName.keySet()) {
                    brains.put(WarAgentType.valueOf(agentName),
                            this.createNewWarBrainImplementationClass
                                    (defaultClassPool,
                                            teamConfigReader.getBrainsPackageName()
                                                    + "."
                                                    + brainControllersClassesName.get(agentName)));
                }
            }

            String team = teamConfigReader.getTeamName();
            String description = teamConfigReader.getTeamDescription();
            ImageIcon imageIcon = (l.length > 0) ? new ImageIcon(WarIOTools.toByteArray
                    (new FileInputStream(l[0]))) : null;

            System.out.println("teamName: " + team);
            System.out.println("teamDesc: " + description);
            System.out.println((imageIcon != null) ? "have Image" : "have no image");

            Team currentTeam = new JavaTeam(
                    team,
                    description,
                    imageIcon,
                    brains);
            return currentTeam;
        }
        throw new IOException("File classpath:" + teamsSourcesFolders.get(teamConfigReader.getTeamName())
                + " not found");
    }

    private Class<? extends WarBrain> createNewWarBrainImplementationClass(ClassPool classPool, String brainClassName) throws NotFoundException, CannotCompileException, IOException {
        try {
            WarExplorerBrainController.class.getClassLoader().loadClass(WarBrainImplementation.class.getCanonicalName());
            WarExplorerBrainController.class.getClassLoader().loadClass(AgentBrainImplementer.class.getCanonicalName());
            WarExplorerBrainController.class.getClassLoader().loadClass(WarBrain.class.getCanonicalName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ClassClassPath ccpath = new ClassClassPath(WarBrainImplementation.class);
        classPool.insertClassPath(ccpath);
        ccpath = new ClassClassPath(AgentBrainImplementer.class);
        classPool.insertClassPath(ccpath);
        CtClass brainImplementationClass = classPool.get(WarBrainImplementation.class.getCanonicalName());

        if (brainImplementationClass.isFrozen())
            brainImplementationClass.defrost();

        if (brainImplementationClass.isFrozen())
            return null;

        brainImplementationClass.setName(brainClassName + "BrainImplementation");
        brainImplementationClass.setModifiers(1);
        CtClass brainClass = classPool.get(brainClassName);
        String capacitiesPackageName = Agressive.class.getPackage().getName();
        CtClass[] arr$ = brainClass.getSuperclass().getInterfaces();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            CtClass brainInterface = arr$[i$];
            if (brainInterface.getPackageName().equals(capacitiesPackageName)) {
                CtClass brainInterfaceImplementation = classPool.get
                        (WarBrainImplementation.class.getPackage().getName() +
                                ".War" +
                                brainInterface.getSimpleName() +
                                "BrainImplementation");
                CtMethod[] arr$1 = brainInterface.getDeclaredMethods();
                int len$1 = arr$1.length;

                for (int i$1 = 0; i$1 < len$1; ++i$1) {
                    CtMethod interfaceImplementationMethod = arr$1[i$1];
                    brainImplementationClass.addMethod(
                            new CtMethod
                                    (brainInterfaceImplementation.
                                            getDeclaredMethod
                                                    (interfaceImplementationMethod.getName(),
                                                            interfaceImplementationMethod.getParameterTypes()),
                                            brainImplementationClass,
                                            null));
                }
            }
        }
        brainImplementationClass.setSuperclass(brainClass);
        Class<?> constructClass = classPool.toClass(brainImplementationClass, WarExplorerBrainController.class.getClassLoader(), null);
        return constructClass.asSubclass(WarBrain.class);
    }
}
