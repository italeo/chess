import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.*;
import server.ServerFacade;
import server.SessionManager;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {
    ServerFacade facade = new ServerFacade("http://localhost:8080");

    @BeforeEach
    public void setUp() throws Exception {
        facade.clear();
    }

    @AfterEach
    public void tearDown() throws Exception {
        facade.clear();
    }

    @Test
    public void registerSuccess_Test() throws Exception {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Check the result to see if it is null indicating a status code of 200
        RegisterResult result = facade.registerUser(request);
        assertNull(result.getMessage());
        assertTrue(result.isSuccess());
    }

    @Test
    public void registerFail_Test() throws
            Exception {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Add a new user
        facade.registerUser(request);
        // Try adding the same user again
        assertThrows(Exception.class, ()-> facade.registerUser(request));
    }

    @Test
    public void loginSuccess_Test() throws Exception {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        facade.registerUser(request);
        // Create a login request
        LoginRequest loginRequest = new LoginRequest("italeo", "password");
        // login
        LoginResult result = facade.login(loginRequest);
        assertNull(result.getMessage());
        assertTrue(result.isSuccess());
    }

    @Test
    public void loginFail_Test() throws Exception {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        facade.registerUser(request);
        // Create a bad login request
        LoginRequest loginRequest = new LoginRequest(null, null);
        // login
        assertThrows(Exception.class, ()-> facade.login(loginRequest));

    }


    @Test
    public void createGameSuccess_Test() throws Exception {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create a request for the game
        CreateGameRequest gameRequest = new CreateGameRequest(result.getAuthToken(), "game1");
        CreateGameResult gameResult = facade.createGame(gameRequest);
        assertNull(gameResult.getMessage());
        assertTrue(gameResult.isSuccess());

    }

    @Test
    public void createGameFail_Test() throws Exception {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create a bad request for the game
        CreateGameRequest gameRequest = new CreateGameRequest(result.getAuthToken(), null);
        // checking if it throws an exception
        assertThrows(Exception.class, ()-> facade.createGame(gameRequest));

    }

    @Test
    public void listGameSuccess_Test() throws Exception {
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create game requests for the games
        CreateGameRequest gameRequest0 = new CreateGameRequest(result.getAuthToken(), "game");
        CreateGameRequest gameRequest1 = new CreateGameRequest(result.getAuthToken(), "game1");
        CreateGameRequest gameRequest2 = new CreateGameRequest(result.getAuthToken(), "game2");
        CreateGameRequest gameRequest3 = new CreateGameRequest(result.getAuthToken(), "game3");

        // Create the games
        facade.createGame(gameRequest0);
        facade.createGame(gameRequest1);
        facade.createGame(gameRequest2);
        facade.createGame(gameRequest3);

        // Create a new list game request
        ListGamesRequest listRequest = new ListGamesRequest(result.getAuthToken());

        // list the games
        ListGameResult listGameResult = facade.listGames(listRequest);
        assertNull(listGameResult.getMessage());
        assertTrue(listGameResult.isSuccess());
    }

    // Need to fix
    @Test
    public void listGameFail_Test() throws Exception {

        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create game requests for the games
        CreateGameRequest gameRequest0 = new CreateGameRequest(result.getAuthToken(), "game");
        CreateGameRequest gameRequest1 = new CreateGameRequest(result.getAuthToken(), "game1");
        CreateGameRequest gameRequest2 = new CreateGameRequest(result.getAuthToken(), "game2");
        CreateGameRequest gameRequest3 = new CreateGameRequest(result.getAuthToken(), "game3");

        // Create the games
        facade.createGame(gameRequest0);
        facade.createGame(gameRequest1);
        facade.createGame(gameRequest2);
        facade.createGame(gameRequest3);

        ListGamesRequest listRequest = new ListGamesRequest(null);

        // Check to see that it actually throws an exception
        assertThrows(Exception.class, ()-> facade.listGames(listRequest));

    }

    @Test
    public void observeGameSuccess_Test() throws Exception {
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create a game request
        CreateGameRequest gameRequest0 = new CreateGameRequest(result.getAuthToken(), "game");
        // Create the game
        CreateGameResult gameResult = facade.createGame(gameRequest0);

        // Create a join game request to join as an observer
        JoinGameRequest joinRequest = new JoinGameRequest(result.getAuthToken(), null, gameResult.getGameID());
        // join the game as an observer
        // Check that there is no message which signifies a status code 200
        JoinGameResult observeResult = facade.joinGame(joinRequest);
        assertNull(observeResult.getMessage());
        assertTrue(observeResult.isSuccess());
    }

    @Test
    public void observeGameFail_Test() throws Exception {
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create a game request
        CreateGameRequest gameRequest0 = new CreateGameRequest(result.getAuthToken(), "game");
        // Create the game
        CreateGameResult gameResult = facade.createGame(gameRequest0);
        // Create a join game request to join as an observer with a bad authToken
        JoinGameRequest joinRequest = new JoinGameRequest(null, null, gameResult.getGameID());

        // join the game as an observer, checking that an exception is thrown
        assertThrows(Exception.class, ()->facade.joinGame(joinRequest));
    }

    @Test
    public void joinGameSuccess_Test() throws Exception {
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create a game request
        CreateGameRequest gameRequest0 = new CreateGameRequest(result.getAuthToken(), "game");
        CreateGameRequest gameRequest1 = new CreateGameRequest(result.getAuthToken(), "game1");
        // Create the game
        CreateGameResult gameResult = facade.createGame(gameRequest0);
        CreateGameResult gameResult1 = facade.createGame(gameRequest1);

        // Create a join game request to join as a White player
        JoinGameRequest joinRequest = new JoinGameRequest(result.getAuthToken(), "WHITE", gameResult.getGameID());
        // Create a join game request to join as a Black player
        JoinGameRequest joinRequest1 = new JoinGameRequest(result.getAuthToken(), "BLACK", gameResult1.getGameID());
        // join the game as an observer
        // Check that there is no message which signifies a status code 200
        // Checking for White user here
        assertNull(facade.joinGame(joinRequest).getMessage());
        // Checking for Black user here
        assertNull(facade.joinGame(joinRequest1).getMessage());

    }

    @Test
    public void joinGameFail_Test() throws Exception {
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create a game request
        CreateGameRequest gameRequest0 = new CreateGameRequest(result.getAuthToken(), "game");
        // Create the game
        CreateGameResult gameResult = facade.createGame(gameRequest0);

        // Create a join game request to join with a null authToken
        JoinGameRequest joinRequest = new JoinGameRequest(null, null, gameResult.getGameID());

        // Check to see that an exception is thrown
        assertThrows(Exception.class, ()-> facade.joinGame(joinRequest));
    }

    @Test
    public void logoutSuccess_Test() throws Exception {
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Test the logout with new logout request
        LogoutRequest logoutRequest = new LogoutRequest(result.getAuthToken());
        assertNull(facade.logout(logoutRequest).getMessage());
    }

    @Test
    public void logoutFail_Test() throws Exception {
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        String authToken = result.getAuthToken();
        SessionManager.setAuthToken(authToken);
        // Test the logout with bad authToken
        SessionManager.setAuthToken(null);
        LogoutRequest logoutRequest = new LogoutRequest(SessionManager.getAuthToken());
        assertThrows(Exception.class, ()-> facade.logout(logoutRequest));
    }
}


