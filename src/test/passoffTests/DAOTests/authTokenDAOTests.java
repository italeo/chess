package passoffTests.DAOTests;

import dao.AuthTokenDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class authTokenDAOTests {
    // The authToken DAO
    private AuthTokenDAO authTokenDAO;
    private final Database db = new Database();
    // The connection to connect to the db
    private Connection conn;
    private AuthToken testToken;

    @BeforeEach
    public void setUp() throws DataAccessException {
        conn = db.getConnection();
        authTokenDAO = new AuthTokenDAO(conn);
        testToken = new AuthToken("validToken", "Lazarus");
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        db.returnConnection(conn);
    }

    @Test
    public void insertTest_Success() throws DataAccessException {
        // Create an authToken and try inserting it
        authTokenDAO.insert(testToken);

        // Make sure that the token is in there
        assertEquals("validToken", authTokenDAO.find("validToken").getAuthToken());
        assertEquals("Lazarus", authTokenDAO.find("validToken").getUsername());
    }

    @Test
    public void insertTest_Fail() {
        // Create a bad authToken
        AuthToken testToken1 = new AuthToken(null, "Ishmael");

        // Test that the right exceptions is thrown.
        assertThrows(DataAccessException.class, () -> authTokenDAO.insert(testToken1));

        // Check that it is actually not in there
        assertThrows(NullPointerException.class, () -> authTokenDAO.find(null).getUsername());
    }

    @Test
    public void clearTest_Success() throws DataAccessException {
        // Create a few authToken and added them into the db
        AuthToken token1 = new AuthToken("123-fght-yh3", "Steven");
        AuthToken token2 = new AuthToken("897-fght-yh3", "Kevin");
        AuthToken token3 = new AuthToken("123-bmnv-yh3", "Jim");

        // Add the tokens into the db
        authTokenDAO.insert(token1);
        authTokenDAO.insert(token2);
        authTokenDAO.insert(token3);

        // Check it is in the db
        authTokenDAO.find("123-fght-yh3");
        authTokenDAO.find("897-fght-yh3");
        authTokenDAO.find("123-bmnv-yh3");

        // Clear the db
        authTokenDAO.clear();

        // Check that the db is actually clear
        assertNull(authTokenDAO.find("123-fght-yh3"));
        assertNull(authTokenDAO.find("897-fght-yh3"));
        assertNull(authTokenDAO.find("123-bmnv-yh3"));
    }

    @Test
    public void findTest_Success() throws DataAccessException {
        // Insert a testToken and try to find it
        authTokenDAO.insert(testToken);

        // Test that it is actually there
        assertEquals("validToken", authTokenDAO.find("validToken").getAuthToken());

        // Try adding a few more tokens and searching for them
        AuthToken token1 = new AuthToken("123-fght-yh3", "Steven");
        AuthToken token2 = new AuthToken("897-fght-yh3", "Kevin");
        AuthToken token3 = new AuthToken("123-bmnv-yh3", "Jim");

        // Now inserting the tokens
        authTokenDAO.insert(token1);
        authTokenDAO.insert(token2);
        authTokenDAO.insert(token3);

        // Test that the find method is working
        assertEquals("123-fght-yh3", authTokenDAO.find("123-fght-yh3").getAuthToken());
        assertEquals("897-fght-yh3", authTokenDAO.find("897-fght-yh3").getAuthToken());
        assertEquals("123-bmnv-yh3", authTokenDAO.find("123-bmnv-yh3").getAuthToken());
    }

    @Test
    public void findTest_Fail() throws DataAccessException {
        // Try adding a few more tokens and searching for them
        AuthToken token1 = new AuthToken("123-fght-yh3", "Steven");
        AuthToken badToken = new AuthToken("bad-Token", "Lim");

        // Insert a test token
        authTokenDAO.insert(token1);

        // Try searching for a token that isn't there
        assertNull(authTokenDAO.find("bad-Token"));

        // Double-checking that the token is not in their by trying to find the username
        assertThrows(NullPointerException.class, () -> authTokenDAO.find("bad-Token").getUsername());

    }

    @Test
    public void deleteTest_Success() throws DataAccessException {
        // Added multiple tokens to test for deleting
        AuthToken token1 = new AuthToken("123-fght-yh3", "Steven");
        AuthToken token2 = new AuthToken("897-fght-yh3", "Kevin");
        AuthToken token3 = new AuthToken("123-bmnv-yh3", "Jim");

        // Insert the tokens
        authTokenDAO.insert(token1);
        authTokenDAO.insert(token2);
        authTokenDAO.insert(token3);

        // Check the tokens are in there
        authTokenDAO.find("123-fght-yh3");
        authTokenDAO.find("897-fght-yh3");
        authTokenDAO.find("123-bmnv-yh3");

        // now delete a token
        authTokenDAO.delete("123-fght-yh3");

        // Check if it is still in the db
        assertThrows(NullPointerException.class, () -> authTokenDAO.find("123-fght-yh3").getAuthToken());
        assertThrows(NullPointerException.class, () -> authTokenDAO.find("123-fght-yh3").getUsername());

        // Checking if the other tokens are still in there
        authTokenDAO.find("897-fght-yh3");
        authTokenDAO.find("123-bmnv-yh3");
    }

    @Test
    public void deleteTest_Fail() throws DataAccessException {
        // Added multiple tokens to test for deleting
        AuthToken token1 = new AuthToken("123-fght-yh3", "Steven");
        AuthToken token2 = new AuthToken("897-fght-yh3", "Kevin");
        AuthToken token3 = new AuthToken("123-bmnv-yh3", "Jim");

        // Insert the tokens
        authTokenDAO.insert(token1);
        authTokenDAO.insert(token2);
        authTokenDAO.insert(token3);

        // Check the tokens are in there
        authTokenDAO.find("123-fght-yh3");
        authTokenDAO.find("897-fght-yh3");
        authTokenDAO.find("123-bmnv-yh3");

        // Now try deleting a token that is not in there
        assertThrows(DataAccessException.class, () -> authTokenDAO.delete(null));

        // Check that the other tokens are still in there
        authTokenDAO.find("123-fght-yh3");
        authTokenDAO.find("897-fght-yh3");
        authTokenDAO.find("123-bmnv-yh3");
    }
}
