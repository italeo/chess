package passoffTests.serviceTests;

import dao.*;
import dataAccess.Database;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private AuthTokenDAO authTokenDAO;
    private UserDAO userDAO;
    private LoginService loginService;
    private Connection conn;
    private Database db = new Database();

    // Tests if joining a game is successful
    // We do so but creating a valid authToken and a valid game, then add that into our db
    // Create a valid request and check with assertions if the service class works accordingly
    @BeforeEach
    public void setUp() throws dataAccess.DataAccessException {
        conn = db.getConnection();
        authTokenDAO = new AuthTokenDAO(conn);
        userDAO = new UserDAO(conn);
        loginService = new LoginService(authTokenDAO, userDAO);
    }

    // Setting for the test by creating the db, and needed components for testing
    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        userDAO.clear();
        db.returnConnection(conn);
    }

    // Created a user that is already in the userDAO/db, which mimicking that the user has already registered and so we are trying to
    // log in that same user with a valid request
    @Test
    public void loginTest_Success() throws DataAccessException, dataAccess.DataAccessException {
        User user = new User("Ishmael", "password", "it@gmail.com");
        userDAO.insert(user);

        LoginRequest request = new LoginRequest("Ishmael", "password");
        LoginResult result = loginService.login(request);

        assertTrue(result.isSuccess());
        assertEquals(request.getUsername(), result.getUsername());
        assertNotNull(result.getAuthToken());
    }

    // Testing that a user that has not registered to try log in, and making sure that the service is handling that correctly
    @Test
    public void loginTest_Unauthorized() throws DataAccessException, dataAccess.DataAccessException {
        User user = new User("Ishmael", "password", "it@gmail.com");
        userDAO.insert(user);

        LoginRequest request = new LoginRequest(null, "password");
        LoginResult result = loginService.login(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: unauthorized", result.getMessage());
    }
}
