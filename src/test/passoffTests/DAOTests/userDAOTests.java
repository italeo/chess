package passoffTests.DAOTests;

import dao.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class userDAOTests {
    // Instance of the userDAO for testing
    private UserDAO userDAO;
    // The db to connect to
    private Database db;
    // The connection to connect to the db
    private Connection conn;
    //Dummy user for testing
    User testUser;

    // Setup before every test we want to initiate the db get the connection and also initiate the UserDAO
    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        conn = db.getConnection();
        userDAO = new UserDAO(conn);
        testUser = new User("italeo", "password", "italeo@gmail.com");
    }

    // Making sure we break down everything properly after each test.
    @AfterEach
    public void tearDown() throws DataAccessException {
        userDAO.clear();
        db.returnConnection(conn);
    }

    // Testing if inserting into the userDAO works
    @Test
    public void insertTest_Success() throws DataAccessException {
        // Insert the user into the MySQL db
        userDAO.insert(testUser);
        User compareResult = userDAO.find(testUser.getUsername());

        // Assert statements to check if:
        // 1. The db is not null
        //2. The right username, password, and email are in the db
        assertNotNull(compareResult);
        assertEquals(testUser.getUsername(), compareResult.getUsername());
        assertEquals(testUser.getPassword(), compareResult.getPassword());
        assertEquals(testUser.getEmail(), compareResult.getEmail());
    }

    @Test
    public void insertTest_Fail() throws DataAccessException {
        // For this test we are checking that situation of inserting the same user twice, and an invalid user.
        User testUser2 = testUser;

        userDAO.insert(testUser);
        assertThrows(DataAccessException.class, ()-> userDAO.insert(testUser2));
    }
}
