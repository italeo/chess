package server;

import model.Game;
import result.ListGameSuccessResult;

import java.util.List;

public class SessionManager {
    private static String authToken;
    private static Integer gameID;
    private static String gameName;
    private static Game game;
    private static List<ListGameSuccessResult> games;

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

    public static String getGameName() {
        return gameName;
    }

    public static void setGameName(String gameName) {
        SessionManager.gameName = gameName;
    }

    public static List<ListGameSuccessResult> getGames() {
        return games;
    }

    public static void setGames(List<ListGameSuccessResult> games) {
        SessionManager.games = games;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        SessionManager.game = game;
    }
}
