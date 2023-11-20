package server;

import result.ListGameSuccessResult;
import java.util.List;

public class SessionManager {
    private static String authToken;
    private static Integer gameID;
    private static String gameName;
    private static List<ListGameSuccessResult> games;
    public static synchronized String getAuthToken() {
        return authToken;
    }
    public static synchronized void setAuthToken(String authToken) {
        SessionManager.authToken = authToken;
    }
    public static synchronized Integer getGameID() {
        return gameID;
    }
    public static synchronized void setGameID(Integer gameID) {
        SessionManager.gameID = gameID;
    }
    public static synchronized String getGameName() { return gameName; }
    public static synchronized void setGameName(String gameName) { SessionManager.gameName = gameName; }
    public static synchronized List<ListGameSuccessResult> getGames() { return games; }
    public static synchronized void setGames(List<ListGameSuccessResult> games) { SessionManager.games = games; }
}
