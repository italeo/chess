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

    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        gameDAO = new GameDAO();
        joinGameService = new JoinGameService(authTokenDAO, gameDAO);
        chessGame = new ChessGameImpl();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        gameDAO.clear();
    }


    @Test
    public void joinGameTest_Success() throws DataAccessException {
        AuthToken token = new AuthToken("validToken", "italeo");
        authTokenDAO.insert(token);

        Game game = new Game(1223, null, null, "chessGame", chessGame);
        gameDAO.insert(game);

        JoinGameRequest request = new JoinGameRequest("validToken", "BLACK", 1223);
        JoinGameResult result = joinGameService.joinGame(request);

        assertTrue(result.isSuccess());

    }

    @Test
    public void joinGameTest_BadRequest() {
        JoinGameRequest request = new JoinGameRequest(null, "WHITE", 1235);

        JoinGameResult result = joinGameService.joinGame(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: bad request", result.getMessage());
    }

    @Test
    public void joinGameTest_Unauthorized() {
        JoinGameRequest request = new JoinGameRequest("invalid-token", "WHITE", 1235);

        JoinGameResult result = joinGameService.joinGame(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: unauthorized", result.getMessage());
    }

    @Test
    public void joinGameTest_Taken() throws DataAccessException{
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
