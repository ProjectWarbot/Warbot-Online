package edu.warbot.process.communication.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.warbot.process.communication.InterProcessMessage;

/**
 * Created by beugnon on 21/04/15.
 *
 * @author beugnon
 */
public class LaunchGameCommand extends InterProcessMessage {

    public final static String HEADER = "LaunchGameCommand";

    public final static String DEFAULT_MAP = "default_map:default";

    public final static String PLAYER_MAP_HEADER = "player_map:";

    public final static String PLAYER_TEAM_HEADER = "player_team:";

    public final static String IA_TEAM_HEADER = "ia_team:";

    public final static String IA_TEAM_RANDOM = "ia_team:*";


    private String team1;

    private String team2;

    private String map;

    protected LaunchGameCommand() {
        super(HEADER);
        this.team1 = IA_TEAM_RANDOM;
        this.team2 = IA_TEAM_RANDOM;
        this.map = DEFAULT_MAP;
    }

    protected LaunchGameCommand(String team1, String team2) {
        this();
        this.team1 = team1;
        this.team2 = team2;
        this.map = DEFAULT_MAP;//Default map
    }

    /**
     * @param team1
     * @param team2
     * @param map
     */
    protected LaunchGameCommand(String team1, String team2, String map) {
        this(team1, team2);
        this.map = map;//Default map
    }

    public final String getTeam1() {
        return team1;
    }

    public final String getTeam2() {
        return team2;
    }

    public final String getMap() {
        return map;
    }

    @JsonIgnore
    public boolean isDefaultMap() {
        return getMap().equals(DEFAULT_MAP);
    }

    @JsonIgnore
    public boolean isPlayerMap() {
        return getMap().startsWith(PLAYER_MAP_HEADER);
    }

    @JsonIgnore
    public boolean isIATeam1() {
        return getTeam1().startsWith(IA_TEAM_HEADER);
    }

    @JsonIgnore
    public boolean isIATeam2() {
        return getTeam2().startsWith(IA_TEAM_HEADER);
    }

    public static class LaunchGameCommandBuilder {

        private LaunchGameCommand launch;

        public LaunchGameCommandBuilder() {
            launch = new LaunchGameCommand();
        }

        public LaunchGameCommandBuilder setPlayerForTeam1(Long id) {
            launch.team1 = PLAYER_TEAM_HEADER + id;
            return this;
        }

        public LaunchGameCommandBuilder setPlayerForTeam2(Long id) {
            launch.team2 = PLAYER_TEAM_HEADER + id;
            return this;
        }

        public LaunchGameCommandBuilder setValueTeam1(String value) {
            launch.team1 = value;
            return this;
        }

        public LaunchGameCommandBuilder setIAForTeam1(String id) {
            launch.team1 = IA_TEAM_HEADER + id;
            return this;
        }

        public LaunchGameCommandBuilder setIAForTeam2(String id) {
            launch.team2 = IA_TEAM_HEADER + id;
            return this;
        }

        public LaunchGameCommandBuilder setMap(String map) {
            launch.map = PLAYER_MAP_HEADER + map;
            return this;
        }

        public LaunchGameCommand build() {
            return launch;
        }

        public LaunchGameCommandBuilder setValueTeam2(String valueTeam2) {
            launch.team2 = valueTeam2;
            return this;
        }
    }
}
