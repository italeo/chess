import exception.ResponseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import result.CreateGameResult;
import result.RegisterResult;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {
    ServerFacade facade = new ServerFacade("http://localhost:8080");

    @BeforeEach
    public void setUp() throws ResponseException {
        facade.clear();
    }

    @AfterEach
    public void tearDown() throws ResponseException {
        facade.clear();
    }

    @Test
    public void registerSuccess_Test() throws ResponseException {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Check the result to see if it is null indicating a status code of 200
        assertNull(facade.registerUser(request).getMessage());
    }

    @Test
    public void registerFail_Test() throws ResponseException {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");

        // Add a new user
        facade.registerUser(request);

        // Try adding the same user again
        try {
            facade.registerUser(request);
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode());
        }

    }

    @Test
    public void loginSuccess_Test() throws ResponseException {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        facade.registerUser(request);
        // Create a login request
        LoginRequest loginRequest = new LoginRequest("italeo", "password");
        // login
        assertNull(facade.login(loginRequest).getMessage());
    }

    @Test
    public void loginFail_Test() throws ResponseException {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        facade.registerUser(request);
        // Create a bad login request
        LoginRequest loginRequest = new LoginRequest(null, null);
        // login

        try {
            facade.login(loginRequest);
        } catch (ResponseException ex) {
            assertEquals(500, ex.getStatusCode());
        }
    }


    @Test
    public void createGameSuccess_Test() throws ResponseException {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create a request for the game
        CreateGameRequest gameRequest = new CreateGameRequest(result.getAuthToken(), "game1");
        CreateGameResult gameResult = facade.createGame(gameRequest);
        assertNull(gameResult.getMessage());

    }

    @Test
    public void createGameFail_Test() throws ResponseException {
        // Create a dummy request for testing
        RegisterRequest request = new RegisterRequest("italeo", "password", "email");
        // Register the user
        RegisterResult result = facade.registerUser(request);
        // Create a bad request for the game
        CreateGameRequest gameRequest = new CreateGameRequest(result.getAuthToken(), null);
        try {
            CreateGameResult gameResult = facade.createGame(gameRequest);

        } catch (ResponseException ex) {
            assertEquals(500,ex.getStatusCode());
        }
    }

    @Test
    public void joinGameSuccess_Test() {


    }

    @Test
    public void joinGameFail_Test() {

    }
}


