package passoffTests.serviceTests;

import chess.ChessGame;
import chess.ChessGameImpl;
import dao.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.ListGamesRequest;
import result.ListGameResult;
import service.ListGamesService;

import static org.junit.jupiter.api.Assertions.*;

public class ListGameTest {
    private AuthTokenDAO authTokenDAO;
    private GameDAO gameDAO;
    private ListGamesService listGamesService;
    private ChessGame chessGame;

    // Setting for the test by creating the db, and needed components for testing
    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        gameDAO = new GameDAO();
        chessGame = new ChessGameImpl();
        listGamesService = new ListGamesService(authTokenDAO, gameDAO);
    }

    // Making sure we reset everything after every tests
    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        gameDAO.clear();
    }

    // Success test, created a few games added it to the list and see if we are able to display the list using a valid request
    @Test
    public void listGameTest_Success() throws DataAccessException {
        AuthToken token = new AuthToken("validToken", "italeo");
        authTokenDAO.insert(token);

        Game game1 = new Game(1223, null, null, "chessGame", chessGame);
        Game game2 = new Game(1224, null, null, "game", chessGame);
        Game game3 = new Game(1225, null, null, "chess", chessGame);

        gameDAO.insert(game1);
        gameDAO.insert(game2);
        gameDAO.insert(game3);

        ListGamesRequest request = new ListGamesRequest("validToken");

        ListGameResult result = listGamesService.listAvailableGames(request);

        assertTrue(result.isSuccess());
        assertNotNull(result.getGames());

    }

    // Testing to make sure that an unauthorized authToken cannot view the games that are available
    @Test
    public void listGameTest_Unauthorized() throws DataAccessException {
        AuthToken token = new AuthToken("validToken", "italeo");
        authTokenDAO.insert(token);

        Game game1 = new Game(1223, null, null, "chessGame", chessGame);
        Game game2 = new Game(1224, null, null, "game", chessGame);
        Game game3 = new Game(1225, null, null, "chess", chessGame);

        gameDAO.insert(game1);
        gameDAO.insert(game2);
        gameDAO.insert(game3);

        ListGamesRequest request = new ListGamesRequest(null);

        ListGameResult result = listGamesService.listAvailableGames(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: unauthorized", result.getMessage());

    }
}
