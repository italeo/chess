package passoffTests.serviceTests;

import chess.*;
import dao.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.ClearResult;
import service.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearTest {
    private AuthTokenDAO authTokenDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private Connection conn;
    private Database db = new Database();

    // Starts by initiating the authTokenDAO, gameDAO and userDAO
    @BeforeEach
    public void setUp() throws DataAccessException {
        conn = db.getConnection();
        authTokenDAO = new AuthTokenDAO(conn);
        userDAO = new UserDAO(conn);
        gameDAO = new GameDAO(conn);

        ChessGame chessGame = new ChessGameImpl();

        // Creating dummy data to insert into the database to test if clearService works

        //Create user
        User user = new User();
        user.setUsername("italeo");
        user.setPassword("chessGame");
        user.setEmail("it@gmail.com");
        userDAO.insert(user);

        //Create AuthToken
        AuthToken authToken = new AuthToken();
        authToken.setUsername("italeo");
        authToken.setAuthToken("12367-34567-fdgfg-67j6");
        authTokenDAO.insert(authToken);

        //Create Game
        Game game = new Game();
        game.setGame(chessGame);
        game.setGameName("chessGame1");
        game.setBlackUsername("blackUser");
        game.setWhiteUsername("whiteUser");
        game.setGameID(1234);
        gameDAO.insert(game);

    }

    // Just making sure that after every test we clear everything, a bit redundant for sure, especially with clear.
    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        userDAO.clear();
        gameDAO.clear();
        db.returnConnection(conn);
    }

    // Testing with clear works.
    @Test
    public void clearTestSuccess() throws DataAccessException {
        ClearService clearService = new ClearService(gameDAO, userDAO, authTokenDAO);
        ClearResult result = clearService.clear();

        assertTrue(result.isSuccess());
        assertNull(userDAO.find("italeo"));
        assertNull(gameDAO.findGameByID(1234));
        assertNull(authTokenDAO.find("validToken"));
    }
}
