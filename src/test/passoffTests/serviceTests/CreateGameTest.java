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

    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        gameDAO = new GameDAO();
        createGameService = new CreateGameService(authTokenDAO, gameDAO);

    }
    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        gameDAO.clear();
    }


    @Test
    public void createGameTest_Success() throws DataAccessException {
        AuthToken authToken = new AuthToken("valid_token-234-5676-dfg-6g", "italeo");
        authTokenDAO.insert(authToken);

        CreateGameRequest request = new CreateGameRequest("valid_token-234-5676-dfg-6g", "chessGame1");

        CreateGameResult result = createGameService.createGame(request);

        assertTrue(result.isSuccess());
        assertNotNull(result.getGameID());
        assertNull( result.getMessage());
    }

    @Test
    public void createGameTest_BadRequest() {
        CreateGameRequest request = new CreateGameRequest(null, null);

        CreateGameResult result = createGameService.createGame(request);

        assertFalse(result.isSuccess());
        assertNull(result.getGameID());
        assertEquals("Error: bad request", result.getMessage());
    }

    @Test
    public void creatGameTest_Unauthorized() {
        CreateGameRequest request = new CreateGameRequest("invalidToken", "chessGame1");

        CreateGameResult result = createGameService.createGame(request);

        assertFalse(result.isSuccess());
        assertNull(result.getGameID());
        assertEquals( "Error: unauthorized", result.getMessage());

    }
}
