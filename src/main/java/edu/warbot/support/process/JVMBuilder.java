package edu.warbot.support.process;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beugnon on 30/04/15.
 *
 * Cette classe permet de générer des processus de JVM basé sur la variable d'environnement JAVA_HOME
 * Elle permet ainsi de choisir la classe à exécuter avec ses arguments, ses flux d'entrée/sortie
 * ainsi que son classpath.
 * @author beugnon
 */
public class JVMBuilder {

    private static String JAVA_HOME = System.getProperty("java.home");

    public final static String DEFAULT_JAVA = JAVA_HOME+File.separator+"bin"+File.separator;

    private final static String JAVA = "java";

    private final static String JAVAW = "javaw";

    private final static String separator = (File.separatorChar == '/') ? ":" : ";";


    private String command;

    private String mainClass;

    private boolean isWindowed;

    private List<String> classPaths;

    private List<String> arguments;

    private ProcessBuilder pb;

    public JVMBuilder() {
        this.command = DEFAULT_JAVA+File.separator;
        this.classPaths = new ArrayList<String>();
        this.pb = new ProcessBuilder();
    }


    public JVMBuilder addClasspathLibrary(String path) {
        if(!classPaths.contains(path))
            classPaths.add(path);
        return this;
    }

    public JVMBuilder addClasspathByClass(Class<?> klass) throws URISyntaxException, IOException {
        URL url = klass.getProtectionDomain().getCodeSource().getLocation();
        String path = (new File(url.toURI())).getCanonicalPath();
        addClasspathLibrary(path);
        return this;
    }

    public JVMBuilder setMainClass(String classMain) {
        mainClass = classMain;
        return this;
    }

    public JVMBuilder setMainClass(Class<?> classMain) {
        mainClass = classMain.getCanonicalName();
        return this;
    }

    public JVMBuilder setWindowed(boolean windowed) {
        isWindowed = windowed;
        return this;
    }

    public JVMBuilder setArguments(List<String> arguments) {
        this.arguments.addAll(arguments);
        return this;
    }


    public JVMBuilder setInputStream(ProcessBuilder.Redirect redirect) {
        pb.redirectInput(redirect);
        return this;
    }

    public JVMBuilder setOutputStream(ProcessBuilder.Redirect redirect) {
        pb.redirectOutput(redirect);
        return this;
    }

    public JVMBuilder setErrorStreamRedirection(boolean redirect) {
        pb.redirectErrorStream(redirect);
        return this;
    }

    public Process build() throws IOException {
        List<String> commands = new ArrayList<String>();
        commands.add(command + File.separator + ((isWindowed)? JAVAW : JAVA) );
        commands.add("-classpath");
        String clap = "."; //Default classpath
        for(String app : classPaths)
            clap = clap.concat(separator).concat(app);

        commands.add(clap);
        commands.add(mainClass);

        pb = new ProcessBuilder(commands);
        return pb.start();
    }
}
