package passoffTests.DAOTests;

import chess.ChessGame;
import chess.ChessGameImpl;
import dao.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class gameDAOTests {
    private GameDAO gameDAO;
    // The db to connect to
    private Database db;
    // The connection to connect to the db
    private Connection conn;
    //Dummy game for testing
    private Game gameTest;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        conn = db.getConnection();
        gameDAO = new GameDAO(conn);
        ChessGame chess = new ChessGameImpl();
        gameTest = new Game(2402, "Ben", "Philip", "testGame", chess);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        gameDAO.clear();
        db.returnConnection(conn);
    }

    @Test
    public void insertGame_Success() throws DataAccessException {
        // Chess games to be added to gameDAO
        ChessGame chess1 = new ChessGameImpl();
        ChessGame chess2 = new ChessGameImpl();
        ChessGame chess3 = new ChessGameImpl();

        // First create several games
        Game game1 = new Game(333, "Lucus", "Ben", "game1", chess1);
        Game game2 = new Game(2234, "Rachel", "Broke", "game2", chess2);
        Game game3 = new Game(1990, "Jones", "Kevin", "game3", chess3);

        // Insert the games in to db
        gameDAO.insert(gameTest);
        gameDAO.insert(game1);
        gameDAO.insert(game2);
        gameDAO.insert(game3);

        // Checking if the games are actually in the db
        assertEquals(333, gameDAO.findGameByID(333).getGameID());
        assertNotNull(gameDAO.findGameByID(333).getGame());
        assertEquals("game1", gameDAO.findGameByID(333).getGameName());
        assertEquals("Lucus", gameDAO.findGameByID(333).getWhiteUsername());
        assertEquals("Ben", gameDAO.findGameByID(333).getBlackUsername());

        assertEquals(2234, gameDAO.findGameByID(2234).getGameID());
        assertNotNull(gameDAO.findGameByID(2234).getGame());
        assertEquals("game2", gameDAO.findGameByID(2234).getGameName());
        assertEquals("Rachel", gameDAO.findGameByID(2234).getWhiteUsername());
        assertEquals("Broke", gameDAO.findGameByID(2234).getBlackUsername());

        assertEquals(1990, gameDAO.findGameByID(1990).getGameID());
        assertNotNull(gameDAO.findGameByID(1990).getGame());
        assertEquals("game3", gameDAO.findGameByID(1990).getGameName());
        assertEquals("Jones", gameDAO.findGameByID(1990).getWhiteUsername());
        assertEquals("Kevin", gameDAO.findGameByID(1990).getBlackUsername());

        assertEquals(2402, gameDAO.findGameByID(2402).getGameID());
        assertNotNull(gameDAO.findGameByID(2402).getGame());
        assertEquals("testGame", gameDAO.findGameByID(2402).getGameName());
        assertEquals("Ben", gameDAO.findGameByID(2402).getWhiteUsername());
        assertEquals("Philip", gameDAO.findGameByID(2402).getBlackUsername());
    }

    @Test
    public void insertGame_Fail() throws DataAccessException {
        // Testing to see what would happen if we insert a game twice.
        // Insert the testGame

        gameDAO.insert(gameTest);

        //Check that the game is in there
        assertEquals(2402, gameDAO.findGameByID(2402).getGameID());
        assertNotNull(gameDAO.findGameByID(2402).getGame());
        assertEquals("testGame", gameDAO.findGameByID(2402).getGameName());
        assertEquals("Ben", gameDAO.findGameByID(2402).getWhiteUsername());
        assertEquals("Philip", gameDAO.findGameByID(2402).getBlackUsername());

        // Try inserting the game again with catching the exception
        assertThrows(DataAccessException.class, () -> gameDAO.insert(gameTest));

    }

    @Test
    public void clearGame_Success() throws DataAccessException {

        // Chess games to be added to gameDAO
        ChessGame chess1 = new ChessGameImpl();
        ChessGame chess2 = new ChessGameImpl();
        ChessGame chess3 = new ChessGameImpl();

        // First create several games
        Game game1 = new Game(333, "Lucus", "Ben", "game1", chess1);
        Game game2 = new Game(2234, "Rachel", "Broke", "game2", chess2);
        Game game3 = new Game(1990, "Jones", "Kevin", "game3", chess3);

        // Insert a bunch of games
        gameDAO.insert(game1);
        gameDAO.insert(game2);
        gameDAO.insert(game3);

        // Checking if the games are in the db
        assertEquals(333, gameDAO.findGameByID(333).getGameID());
        assertNotNull(gameDAO.findGameByID(333).getGame());
        assertEquals("game1", gameDAO.findGameByID(333).getGameName());
        assertEquals("Lucus", gameDAO.findGameByID(333).getWhiteUsername());
        assertEquals("Ben", gameDAO.findGameByID(333).getBlackUsername());

        assertEquals(2234, gameDAO.findGameByID(2234).getGameID());
        assertNotNull(gameDAO.findGameByID(2234).getGame());
        assertEquals("game2", gameDAO.findGameByID(2234).getGameName());
        assertEquals("Rachel", gameDAO.findGameByID(2234).getWhiteUsername());
        assertEquals("Broke", gameDAO.findGameByID(2234).getBlackUsername());

        assertEquals(1990, gameDAO.findGameByID(1990).getGameID());
        assertNotNull(gameDAO.findGameByID(1990).getGame());
        assertEquals("game3", gameDAO.findGameByID(1990).getGameName());
        assertEquals("Jones", gameDAO.findGameByID(1990).getWhiteUsername());
        assertEquals("Kevin", gameDAO.findGameByID(1990).getBlackUsername());

        // Clearing the game db
        gameDAO.clear();

        // Checking that the games are no longer there
        assertNull(gameDAO.findGameByID(333));
        assertNull(gameDAO.findGameByID(2234));
        assertNull(gameDAO.findGameByID(1990));
    }

    @Test
    public void findGameByID_Success() throws DataAccessException {
        // Start by adding a few games into the db
        // Chess games to be added to gameDAO
        ChessGame chess1 = new ChessGameImpl();
        ChessGame chess2 = new ChessGameImpl();
        ChessGame chess3 = new ChessGameImpl();

        // First create several games
        Game game1 = new Game(333, "Lucus", "Ben", "game1", chess1);
        Game game2 = new Game(2234, "Rachel", "Broke", "game2", chess2);
        Game game3 = new Game(1990, "Jones", "Kevin", "game3", chess3);

        // Insert a bunch of games
        gameDAO.insert(game1);
        gameDAO.insert(game2);
        gameDAO.insert(game3);

        // Checking if the games are in the db
        assertEquals(333, gameDAO.findGameByID(333).getGameID());
        assertNotNull(gameDAO.findGameByID(333).getGame());
        assertEquals("game1", gameDAO.findGameByID(333).getGameName());
        assertEquals("Lucus", gameDAO.findGameByID(333).getWhiteUsername());
        assertEquals("Ben", gameDAO.findGameByID(333).getBlackUsername());

        assertEquals(2234, gameDAO.findGameByID(2234).getGameID());
        assertNotNull(gameDAO.findGameByID(2234).getGame());
        assertEquals("game2", gameDAO.findGameByID(2234).getGameName());
        assertEquals("Rachel", gameDAO.findGameByID(2234).getWhiteUsername());
        assertEquals("Broke", gameDAO.findGameByID(2234).getBlackUsername());

        assertEquals(1990, gameDAO.findGameByID(1990).getGameID());
        assertNotNull(gameDAO.findGameByID(1990).getGame());
        assertEquals("game3", gameDAO.findGameByID(1990).getGameName());
        assertEquals("Jones", gameDAO.findGameByID(1990).getWhiteUsername());
        assertEquals("Kevin", gameDAO.findGameByID(1990).getBlackUsername());
    }

    @Test
    public void findGameByID_Fail() throws DataAccessException {
        // Start by adding a few games into the db
        // Chess games to be added to gameDAO
        ChessGame chess1 = new ChessGameImpl();
        ChessGame chess2 = new ChessGameImpl();
        ChessGame chess3 = new ChessGameImpl();

        // First create several games
        Game game1 = new Game(333, "Lucus", "Ben", "game1", chess1);
        Game game2 = new Game(2234, "Rachel", "Broke", "game2", chess2);
        Game game3 = new Game(1990, "Jones", "Kevin", "game3", chess3);

        // Insert a bunch of games
        gameDAO.insert(game1);
        gameDAO.insert(game2);
        gameDAO.insert(game3);

        // Checking that the games are in there
        assertEquals(333, gameDAO.findGameByID(333).getGameID());
        assertNotNull(gameDAO.findGameByID(333).getGame());
        assertEquals("game1", gameDAO.findGameByID(333).getGameName());
        assertEquals("Lucus", gameDAO.findGameByID(333).getWhiteUsername());
        assertEquals("Ben", gameDAO.findGameByID(333).getBlackUsername());

        assertEquals(2234, gameDAO.findGameByID(2234).getGameID());
        assertNotNull(gameDAO.findGameByID(2234).getGame());
        assertEquals("game2", gameDAO.findGameByID(2234).getGameName());
        assertEquals("Rachel", gameDAO.findGameByID(2234).getWhiteUsername());
        assertEquals("Broke", gameDAO.findGameByID(2234).getBlackUsername());

        assertEquals(1990, gameDAO.findGameByID(1990).getGameID());
        assertNotNull(gameDAO.findGameByID(1990).getGame());
        assertEquals("game3", gameDAO.findGameByID(1990).getGameName());
        assertEquals("Jones", gameDAO.findGameByID(1990).getWhiteUsername());
        assertEquals("Kevin", gameDAO.findGameByID(1990).getBlackUsername());

        // Search up a game that does not exist and test if it throws the DataAccessException
        // The actual test
        assertThrows(NullPointerException.class, () -> gameDAO.findGameByID(240).getGameID());
    }

    @Test
    public void updateGame_Success() throws DataAccessException {

        // Start by adding a few games into the db
        // Chess games to be added to gameDAO
        ChessGame chess1 = new ChessGameImpl();
        ChessGame chess2 = new ChessGameImpl();
        ChessGame chess3 = new ChessGameImpl();

        // First create several games
        Game game1 = new Game(333, null, null, "game1", chess1);
        Game game2 = new Game(2234, "Rachel", "Broke", "game2", chess2);
        Game game3 = new Game(1990, "Kevin", null, "game3", chess3);

        // Insert a bunch of games
        gameDAO.insert(game1);
        gameDAO.insert(game2);
        gameDAO.insert(game3);

        // Check that the game is in the db
        // Checking that the games are in there
        assertEquals(333, gameDAO.findGameByID(333).getGameID());
        assertNotNull(gameDAO.findGameByID(333).getGame());
        assertEquals("game1", gameDAO.findGameByID(333).getGameName());
        assertNull(gameDAO.findGameByID(333).getWhiteUsername());
        assertNull(gameDAO.findGameByID(333).getBlackUsername());

        assertEquals(2234, gameDAO.findGameByID(2234).getGameID());
        assertNotNull(gameDAO.findGameByID(2234).getGame());
        assertEquals("game2", gameDAO.findGameByID(2234).getGameName());
        assertEquals("Rachel", gameDAO.findGameByID(2234).getWhiteUsername());
        assertEquals("Broke", gameDAO.findGameByID(2234).getBlackUsername());

        assertEquals(1990, gameDAO.findGameByID(1990).getGameID());
        assertNotNull(gameDAO.findGameByID(1990).getGame());
        assertEquals("game3", gameDAO.findGameByID(1990).getGameName());
        assertEquals("Kevin", gameDAO.findGameByID(1990).getWhiteUsername());
        assertNull(gameDAO.findGameByID(1990).getBlackUsername());

        // Insert the new data/changes
        game1.setWhiteUsername("Mike");
        game1.setBlackUsername("Karen");
        game2.setWhiteUsername("Lim");
        game3.setBlackUsername("Scott");

        // Perform the updates
        gameDAO.updateGame(game1);
        gameDAO.updateGame(game2);
        gameDAO.updateGame(game3);

        // Check if those updates are stored correctly
        assertEquals(333, gameDAO.findGameByID(333).getGameID());
        assertNotNull(gameDAO.findGameByID(333).getGame());
        assertEquals("game1", gameDAO.findGameByID(333).getGameName());
        assertEquals("Mike", gameDAO.findGameByID(333).getWhiteUsername());
        assertEquals("Karen", gameDAO.findGameByID(333).getBlackUsername());

        assertEquals(2234, gameDAO.findGameByID(2234).getGameID());
        assertNotNull(gameDAO.findGameByID(2234).getGame());
        assertEquals("game2", gameDAO.findGameByID(2234).getGameName());
        assertEquals("Lim", gameDAO.findGameByID(2234).getWhiteUsername());
        assertEquals("Broke", gameDAO.findGameByID(2234).getBlackUsername());

        assertEquals(1990, gameDAO.findGameByID(1990).getGameID());
        assertNotNull(gameDAO.findGameByID(1990).getGame());
        assertEquals("game3", gameDAO.findGameByID(1990).getGameName());
        assertEquals("Kevin", gameDAO.findGameByID(1990).getWhiteUsername());
        assertEquals("Scott", gameDAO.findGameByID(1990).getBlackUsername());
    }

    @Test
    public void updateGame_Fail() throws DataAccessException {

        // Insert the test game and check if it is there
        gameDAO.insert(gameTest);

        // Check that the information is actually in the db
        assertEquals(2402, gameDAO.findGameByID(2402).getGameID());
        assertNotNull(gameDAO.findGameByID(2402).getGame());
        assertEquals("testGame", gameDAO.findGameByID(2402).getGameName());
        assertEquals("Ben", gameDAO.findGameByID(2402).getWhiteUsername());
        assertEquals("Philip", gameDAO.findGameByID(2402).getBlackUsername());

        // Create a chess game object
        ChessGame chess1 = new ChessGameImpl();

        // Create another test game that is not in the db
        Game game1 = new Game(333, "Lucus", "Ben", "game1", chess1);

        // Test that the new game is not in the db
        assertNull(gameDAO.findGameByID(333));
    }

    @Test
    public void getAllGames_Success() throws DataAccessException {
        // Start by adding a few games into the db
        // Chess games to be added to gameDAO
        ChessGame chess1 = new ChessGameImpl();
        ChessGame chess2 = new ChessGameImpl();
        ChessGame chess3 = new ChessGameImpl();

        // First create several games
        Game game1 = new Game(333, "Lucus", "Ben", "game1", chess1);
        Game game2 = new Game(2234, "Rachel", "Broke", "game2", chess2);
        Game game3 = new Game(1990, "Jones", "Kevin", "game3", chess3);

        // Insert a bunch of games
        gameDAO.insert(game1);
        gameDAO.insert(game2);
        gameDAO.insert(game3);

        // Check if those updates are stored correctly
        assertEquals(333, gameDAO.findGameByID(333).getGameID());
        assertNotNull(gameDAO.findGameByID(333).getGame());
        assertEquals("game1", gameDAO.findGameByID(333).getGameName());
        assertEquals("Lucus", gameDAO.findGameByID(333).getWhiteUsername());
        assertEquals("Ben", gameDAO.findGameByID(333).getBlackUsername());

        assertEquals(2234, gameDAO.findGameByID(2234).getGameID());
        assertNotNull(gameDAO.findGameByID(2234).getGame());
        assertEquals("game2", gameDAO.findGameByID(2234).getGameName());
        assertEquals("Rachel", gameDAO.findGameByID(2234).getWhiteUsername());
        assertEquals("Broke", gameDAO.findGameByID(2234).getBlackUsername());

        assertEquals(1990, gameDAO.findGameByID(1990).getGameID());
        assertNotNull(gameDAO.findGameByID(1990).getGame());
        assertEquals("game3", gameDAO.findGameByID(1990).getGameName());
        assertEquals("Jones", gameDAO.findGameByID(1990).getWhiteUsername());
        assertEquals("Kevin", gameDAO.findGameByID(1990).getBlackUsername());

        // store the list of games for testing
        List<Game> gameList = gameDAO.getAllGames();

        // Verify that the list contains the right number of games
        assertEquals(3, gameList.size());
    }

    @Test
    public void getAllGames_Fail() throws DataAccessException {

        // Helper list
        List<Game> gameList = new ArrayList<>();

        // Added some games to list
        gameDAO.insert(gameTest);

        // Check that the game is in there
        assertEquals(2402, gameDAO.findGameByID(2402).getGameID());
        assertNotNull(gameDAO.findGameByID(2402).getGame());
        assertEquals("testGame", gameDAO.findGameByID(2402).getGameName());
        assertEquals("Ben", gameDAO.findGameByID(2402).getWhiteUsername());
        assertEquals("Philip", gameDAO.findGameByID(2402).getBlackUsername());

        // Check that there is one game in the list
        gameList = gameDAO.getAllGames();

        assertEquals(1, gameList.size());


        // Now clear the db and test there is nothing in there
        gameDAO.clear();

        // Try list an empty gameList
        gameList = gameDAO.getAllGames();

        // Check that the gameList is empty
        assertEquals(0, gameList.size());
    }
}
