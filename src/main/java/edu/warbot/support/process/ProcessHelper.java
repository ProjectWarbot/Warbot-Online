package edu.warbot.support.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by beugnon on 05/05/15.
 */
public class ProcessHelper {


    private static Logger log = LoggerFactory.getLogger(ProcessHelper.class);

    private List<Process> processes;


    public ProcessHelper() {
        processes = new ArrayList<Process>();

    }

    public Process startNewJavaProcess(final String optionsAsString, final String mainClass, final String[] arguments)
            throws IOException {

        ProcessBuilder processBuilder = createProcess(optionsAsString, mainClass, arguments);
        Process process = processBuilder.start();
        processes.add(process);
        log.info("Process " + process.toString() + " has started");
        return process;
    }


    public ProcessBuilder createProcess(final String optionsAsString, final String mainClass, final String[] arguments) {
        String jvm = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");

        log.info("classpath: " + classpath);
        String workingDirectory = System.getProperty("user.dir");
        log.info("workingDirectory: " + workingDirectory);
        String[] options = optionsAsString.split(" ");
        List<String> command = new ArrayList<String>();
        command.add(jvm);
        if (optionsAsString.length() > 0)
            command.addAll(Arrays.asList(options));
        command.add(mainClass);
        command.addAll(Arrays.asList(arguments));
        log.info("Show: "+command+ " ("+command.size()+")");
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Map<String, String> environment = processBuilder.environment();
        environment.put("CLASSPATH",classpath);
        return processBuilder;
    }


    public void killProcess(final Process process) {
        process.destroy();
    }

    /**
     * Kill all processes.
     */
    public void shutdown() {
        log.debug("Killing " + processes.size() + " processes.");
        for (Process process : processes) {
            killProcess(process);
        }
    }

}
