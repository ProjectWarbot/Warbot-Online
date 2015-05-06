package edu.warbot.process.communication;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.warbot.process.communication.client.*;
import edu.warbot.process.communication.server.LaunchGameCommand;
import edu.warbot.process.communication.server.PreciseAgentCommand;
import edu.warbot.process.communication.server.RemoteConsoleCommand;
import edu.warbot.process.communication.server.RemoteWarbotCommand;
import edu.warbot.process.exception.UnrecognizedInterProcessMessageException;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by beugnon on 30/04/15.
 *
 */
public class JSONInterProcessMessageTranslater {

    /**
     * Convert a json message into an instance of InterProcessMessage
     * @param message
     * @return
     * @throws edu.warbot.process.exception.UnrecognizedInterProcessMessageException
     * @throws java.io.IOException
     */
    public static InterProcessMessage convertIntoObject(String message) throws UnrecognizedInterProcessMessageException, IOException {
        ObjectMapper om = new ObjectMapper();
        HashMap<String,Object> object = om.readValue(message,
                new TypeReference<HashMap<String, Object>>() {
                });
        String header = (String) object.get("header");
        switch (header) {
            case PingMessage.HEADER:
                return produce(message,PingMessage.class);
            case LaunchGameCommand.HEADER:
                return produce(message,LaunchGameCommand.class);
            case PreciseAgentCommand.HEADER:
                return produce(message, PreciseAgentCommand.class);
            case RemoteConsoleCommand.HEADER:
                return produce(message, RemoteConsoleCommand.class);
            case RemoteWarbotCommand.HEADER:
                return produce(message,RemoteWarbotCommand.class);
            case AgentMessage.HEADER:
                return produce(message,AgentMessage.class);
            case EndMessage.HEADER:
                return produce(message, EndMessage.class);
            case InitMessage.HEADER:
                return produce(message, InitMessage.class);
            case ExceptionResult.HEADER:
                return produce(message,ExceptionResult.class);
            case PreciseAgentResult.HEADER:
                return produce(message,PreciseAgentResult.class);
            case SynchroMessage.HEADER:
                return produce(message,SynchroMessage.class);
            default:
                throw new UnrecognizedInterProcessMessageException(header);
        }
    }

    public static String convertIntoMessage(InterProcessMessage ipm) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(ipm);
    }

    /**
     * Convert a json message into an instance of class inheritant of InterProcessMessage
     * @param message
     * @return
     * @throws edu.warbot.process.exception.UnrecognizedInterProcessMessageException
     * @throws java.io.IOException
     */
    protected static InterProcessMessage produce(String message,Class<? extends InterProcessMessage> ipmClass) throws IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(message, ipmClass);
    }

    public static boolean isReadableJSON(String message) {
        ObjectMapper om = new ObjectMapper();
        try {
            HashMap<String,Object> object = om.readValue(message,
                    new TypeReference<HashMap<String, Object>>() {
                    });
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
