package passoffTests.serviceTests;

import chess.*;
import dao.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.ClearResult;
import service.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearTest {
    private AuthTokenDAO authTokenDAO;
    private UserDAO userDAO;
    private GameDAO gameDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        authTokenDAO = new AuthTokenDAO();
        userDAO = new UserDAO();
        gameDAO = new GameDAO();

        ChessGame chessGame = new ChessGameImpl();

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

    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }

    @Test
    public void clearTestSuccess() {
        ClearService clearService = new ClearService(gameDAO, userDAO, authTokenDAO);
        ClearResult result = clearService.clear();

        assertTrue(result.isSuccess());

    }
}