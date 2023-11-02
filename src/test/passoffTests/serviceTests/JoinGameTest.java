package passoffTests.serviceTests;

import chess.ChessGame;
import chess.ChessGameImpl;
import dao.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.JoinGameRequest;
import result.JoinGameResult;
import service.JoinGameService;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameTest {
    private AuthTokenDAO authTokenDAO;
    private GameDAO gameDAO;
    private JoinGameService joinGameService;
    private ChessGame chessGame;

    // Setting for the test by creating the db, and needed components for testing
    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        gameDAO = new GameDAO();
        joinGameService = new JoinGameService(authTokenDAO, gameDAO);
        chessGame = new ChessGameImpl();
    }

    // Making sure we reset everything after every tests
    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        gameDAO.clear();
    }

    // Tests if joining a game is successful
    // We do so but creating a valid authToken and a valid game, then add that into our db
    // Create a valid request and check with assertions if the service class works accordingly
    @Test
    public void joinGameTest_Success() throws DataAccessException, dataAccess.DataAccessException {
        AuthToken token = new AuthToken("validToken", "italeo");
        authTokenDAO.insert(token);

        Game game = new Game(1223, null, null, "chessGame", chessGame);
        gameDAO.insert(game);

        JoinGameRequest request = new JoinGameRequest("validToken", "BLACK", 1223);
        JoinGameResult result = joinGameService.joinGame(request);

        assertTrue(result.isSuccess());
    }

    // Check if a bad request is sent the service class handles it accordingly
    @Test
    public void joinGameTest_BadRequest() throws dataAccess.DataAccessException {
        JoinGameRequest request = new JoinGameRequest(null, "WHITE", 1235);

        JoinGameResult result = joinGameService.joinGame(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: bad request", result.getMessage());
    }

    // Check if a unauthorized authToken is trying to join a game
    @Test
    public void joinGameTest_Unauthorized() throws dataAccess.DataAccessException {
        JoinGameRequest request = new JoinGameRequest("invalid-token", "WHITE", 1235);

        JoinGameResult result = joinGameService.joinGame(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: unauthorized", result.getMessage());
    }

    // This is also a negative test that checks if a user has already taken a team color, then no one else should be able to
    @Test
    public void joinGameTest_Taken() throws DataAccessException, dataAccess.DataAccessException {
        AuthToken token = new AuthToken("validToken", "italeo");
        authTokenDAO.insert(token);

        Game game = new Game(1234, "white", "black", "Championship", chessGame);
        gameDAO.insert(game);

        JoinGameRequest request = new JoinGameRequest("validToken", "WHITE", 1234);

        JoinGameResult result = joinGameService.joinGame(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: already taken", result.getMessage());
    }
}
