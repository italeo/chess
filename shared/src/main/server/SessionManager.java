package server;

public class SessionManager {
    private static String authToken;
    private static Integer gameID;
    private static String gameName;
    public static String getAuthToken() {
        return authToken;
    }
    public static void setAuthToken(String authToken) {
        SessionManager.authToken = authToken;
    }
    public static Integer getGameID() {
        return gameID;
    }
    public static void setGameID(Integer gameID) {
        SessionManager.gameID = gameID;
    }
    public static String getGameName() { return gameName; }
    public static void setGameName(String gameName) { SessionManager.gameName = gameName; }
}
