package passoffTests.serviceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.CreateGameService;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameTest {
    private AuthTokenDAO authTokenDAO;
    private GameDAO gameDAO;
    private CreateGameService createGameService;

    // Setting up for test by initiating the necessary DAOs and the creatGameService
    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        gameDAO = new GameDAO();
        createGameService = new CreateGameService(authTokenDAO, gameDAO);
    }

    // Just makes sure we clear it after each test (clearing the database)
    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        gameDAO.clear();
    }

    // Successful test of creat game
    @Test
    public void createGameTest_Success() throws DataAccessException, dataAccess.DataAccessException {

        //Creates an authToken and adds it to the database(db)
        AuthToken authToken = new AuthToken("valid_token-234-5676-dfg-6g", "italeo");
        authTokenDAO.insert(authToken);

        // Dummy request for testing
        CreateGameRequest request = new CreateGameRequest("valid_token-234-5676-dfg-6g", "chessGame1");

        CreateGameResult result = createGameService.createGame(request);

        // Assertions checking if the service class is working as expected
        assertTrue(result.isSuccess());
        assertNotNull(result.getGameID());
        assertNull( result.getMessage());
    }

    // Negative test for the createGame service
    // This test in particular is testing for a bad request, so when the authToken does not exist
    @Test
    public void createGameTest_BadRequest() throws dataAccess.DataAccessException {

        // Sending a bad request
        CreateGameRequest request = new CreateGameRequest(null, null);

        CreateGameResult result = createGameService.createGame(request);

        assertFalse(result.isSuccess());
        assertNull(result.getGameID());
        assertEquals("Error: bad request", result.getMessage());
    }

    // This negative test, tests for a invalid authToken that is trying to create a game.
    @Test
    public void creatGameTest_Unauthorized() throws dataAccess.DataAccessException {
        // Creating the request with an invalid token that might not be in the db
        CreateGameRequest request = new CreateGameRequest("invalidToken", "chessGame1");

        CreateGameResult result = createGameService.createGame(request);

        assertFalse(result.isSuccess());
        assertNull(result.getGameID());
        assertEquals( "Error: unauthorized", result.getMessage());

    }
}
