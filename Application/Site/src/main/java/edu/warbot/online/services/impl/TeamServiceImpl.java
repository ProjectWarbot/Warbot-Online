package edu.warbot.online.services.impl;

import edu.warbot.brains.WarBrain;
import edu.warbot.brains.capacities.Agressive;
import edu.warbot.brains.implementations.AgentBrainImplementer;
import edu.warbot.brains.implementations.WarBrainImplementation;
import edu.warbot.game.Team;
import edu.warbot.launcher.TeamConfigReader;
import edu.warbot.launcher.UserPreferences;
import edu.warbot.online.models.Party;
import edu.warbot.online.models.WebCode;
import edu.warbot.online.repository.PartyRepository;
import edu.warbot.online.repository.WebCodeRepository;
import edu.warbot.online.services.TeamService;
import edu.warbot.scriptcore.ScriptedTeam;
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

    private HashMap<String, Class<? extends WarBrain>> brainControllers = new HashMap<String, Class<? extends WarBrain>>();


    public void loadScriptClasses() {
        try {
            if (brainControllers.isEmpty()) {
                ClassPool defaultClassPool = ClassPool.getDefault();
                brainControllers.put("WarBase", createNewWarBrainImplementationClass(defaultClassPool, "edu.warbot.scriptcore.team.ScriptableWarBase"));
                brainControllers.put("WarExplorer", createNewWarBrainImplementationClass(defaultClassPool, "edu.warbot.scriptcore.team.ScriptableWarExplorer"));
                brainControllers.put("WarEngineer", createNewWarBrainImplementationClass(defaultClassPool, "edu.warbot.scriptcore.team.ScriptableWarEngineer"));
                brainControllers.put("WarRocketLauncher", createNewWarBrainImplementationClass(defaultClassPool, "edu.warbot.scriptcore.team.ScriptableWarRocketLauncher"));
                brainControllers.put("WarKamikaze", createNewWarBrainImplementationClass(defaultClassPool, "edu.warbot.scriptcore.team.ScriptableWarKamikaze"));
                brainControllers.put("WarTurret", createNewWarBrainImplementationClass(defaultClassPool, "edu.warbot.scriptcore.team.ScriptableWarTurret"));
            }
        } catch (NotFoundException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSourceTeams() {
        Map<String, String> teamsSourcesFolders = UserPreferences.getTeamsSourcesFolders();
        for (String currentFolder : teamsSourcesFolders.values()) {
            logger.debug("Introspection in " + currentFolder);
            try {

                InputStream input = WarExplorerBrainController.class.getClassLoader().getResourceAsStream("" + currentFolder + "/" + TeamConfigReader.FILE_NAME);
                if (input != null) {
                    logger.debug("Introspection starting in " + currentFolder);

                    TeamConfigReader teamConfigReader = new TeamConfigReader();
                    teamConfigReader.load(input);
                    input.close();
                    Team e = this.loadTeamFromSources(teamsSourcesFolders, teamConfigReader);

                    if (getTeams().containsKey(e.getName()))
                        logger.error("Erreur lors de la lecture d\'une équipe : le nom " + e.getName() +
                                " est déjà utilisé.");
                    else
                        getTeams().put(e.getName(), e);
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
        if (getBrains().size() == 0)
            loadScriptClasses();
        ScriptedTeam team = new ScriptedTeam(party.getName(), getBrains());
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
            Team currentTeam = new Team(teamConfigReader.getTeamName());

            File l[] = teamDirectory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    return s.equalsIgnoreCase(teamConfigReader.getIconPath());
                }
            });
            if (l != null && l.length == 1)
                currentTeam.setLogo
                        (new ImageIcon(WarIOTools.toByteArray
                                (new FileInputStream(l[0]))));

            currentTeam.setDescription
                    (teamConfigReader.getTeamDescription().trim());
            Map<String, String> brainControllersClassesName = teamConfigReader.
                    getBrainControllersClassesNameOfEachAgentType();

            if (!teamConfigReader.isFSMTeam()) {
                ClassPool defaultClassPool = ClassPool.getDefault();
                for (String agentName : brainControllersClassesName.keySet()) {
                    currentTeam.addBrainControllerClassForAgent
                            (agentName,
                                    this.createNewWarBrainImplementationClass
                                            (defaultClassPool,
                                                    teamConfigReader.getBrainsPackageName()
                                                            + "."
                                                            + brainControllersClassesName.get(agentName)));
                }
            }
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

    private HashMap<String, Class<? extends WarBrain>> getBrains() {
        return brainControllers;
    }
}
