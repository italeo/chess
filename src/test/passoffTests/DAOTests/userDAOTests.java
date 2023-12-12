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
    private User testUser;

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

        // Create other users to double-check that the function works correctly
        User user1 = new User("fish", "swim", "coral@ocean.com");
        User user2 = new User("cat", "paw", "feline@ocean.com");
        User user3 = new User("dog", "walk", "k9@ocean.com");

        // Insert the new users
        userDAO.insert(user1);
        userDAO.insert(user2);
        userDAO.insert(user3);

        // Checking that those user are actually in the db
        assertEquals("fish", userDAO.find("fish").getUsername());
        assertEquals("cat", userDAO.find("cat").getUsername());
        assertEquals("dog", userDAO.find("dog").getUsername());
    }

    @Test
    public void insertTest_Fail() throws DataAccessException {
        // For this test we are checking that situation of inserting the same user twice.
        User testUser2 = testUser;
        userDAO.insert(testUser);

        // Testing to see if we pass in the same username again, it is handled correctly
        assertThrows(DataAccessException.class, ()-> userDAO.insert(testUser2));
    }

    @Test
    public void insertTest_InvalidFail() {

        // Create an invalid user
        User testUser3 = new User(null, "password3", "gmail");

        // Test when we try to insert the user
        assertThrows(DataAccessException.class, () -> userDAO.insert(testUser3));

        // Check that the user actually did not get added by trying to retrieve the email
        assertThrows(NullPointerException.class, () -> userDAO.find(null).getEmail());
    }

    @Test
    public void clearTest_Success() throws DataAccessException {

        // Create a few more users to added to the db for testing
        User user1 = new User("fish", "swim", "coral@ocean.com");
        User user2 = new User("cat", "paw", "feline@ocean.com");
        User user3 = new User("dog", "walk", "k9@ocean.com");

        // Add our test users into the db
        userDAO.insert(testUser);
        userDAO.insert(user1);
        userDAO.insert(user2);
        userDAO.insert(user3);

        // Verify that the table is not empty and contains our test user
        assertEquals("italeo", userDAO.find("italeo").getUsername());
        assertEquals("fish", userDAO.find("fish").getUsername());
        assertEquals("dog", userDAO.find("dog").getUsername());
        assertEquals("cat", userDAO.find("cat").getUsername());

        // Call the clear method
        assertDoesNotThrow(() -> userDAO.clear());

        // Check that the db is actually clear
        assertNull(userDAO.find("italeo"));
        assertNull(userDAO.find("dog"));
        assertNull(userDAO.find("cat"));
        assertNull(userDAO.find("fish"));
    }

    @Test
    public void findTest_Success() throws DataAccessException {
        // Insert the test user into the db to test the find method
        userDAO.insert(testUser);

        // Test the search method
        User test = userDAO.find("italeo");

        // Check if the db is not null
        assertNotNull(test);

        // Creating other test users insert and see if we can find them
        User user1 = new User("John", "swim", "coral@ocean.com");
        User user2 = new User("Ben", "paw", "feline@gmail.com");
        User user3 = new User("Luke", "walk", "k9@yahoo.com");

        // Add our test users into the db
        userDAO.insert(user1);
        userDAO.insert(user2);
        userDAO.insert(user3);

        // Testing the find method
        assertEquals("John", userDAO.find("John").getUsername());
        assertEquals("swim", userDAO.find("John").getPassword());
        assertEquals("coral@ocean.com", userDAO.find("John").getEmail());

        assertEquals("Ben", userDAO.find("Ben").getUsername());
        assertEquals("paw", userDAO.find("Ben").getPassword());
        assertEquals("feline@gmail.com", userDAO.find("Ben").getEmail());

        assertEquals("Luke", userDAO.find("Luke").getUsername());
        assertEquals("walk", userDAO.find("Luke").getPassword());
        assertEquals("k9@yahoo.com", userDAO.find("Luke").getEmail());

    }

    @Test
    public void findTest_fail() throws DataAccessException {

        // Creating a user that does not exist in the db
        User nonExistingUser = null;

        // Add the test user into the db
        userDAO.insert(testUser);

        // Try to find the nonExistingUser from the db
        try {
            nonExistingUser = userDAO.find("Ishmael");
        } catch (DataAccessException e) {
            fail("Exception should not be thrown");
        }

        // Check that the retrieved user is null
        assertNull(nonExistingUser);
    }

}
