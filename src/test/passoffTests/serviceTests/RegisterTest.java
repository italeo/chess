package passoffTests.serviceTests;

import dao.*;
import dataAccess.Database;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    private AuthTokenDAO authTokenDAO;
    private UserDAO userDAO;
    private RegisterService registerService;
    private Connection conn;
    private Database db = new Database();

    // Setting for the test by creating the db, and needed components for testing
    @BeforeEach
    public void setUp() throws dataAccess.DataAccessException {
        conn = db.getConnection();
        authTokenDAO = new AuthTokenDAO(conn);
        userDAO = new UserDAO(conn);
        registerService = new RegisterService(authTokenDAO, userDAO);
    }

    // Making sure we reset everything after every tests
    @AfterEach
    public void tearDown() throws DataAccessException {
        userDAO.clear();
        authTokenDAO.clear();
        db.returnConnection(conn);
    }

    // This test we are creating a completely new user and add that to the db, then we just check to make sure if that is
    // done correctly
    @Test
    public void registerTest_Success() throws dataAccess.DataAccessException {
        RegisterRequest request = new RegisterRequest("Ishmael", "password", "email");
        RegisterResult result = registerService.register(request);

        assertTrue(result.isSuccess());
        assertEquals(request.getUsername(), result.getUsername());
        assertNotNull(result.getAuthToken());
    }

    // Testing if a user is registering with a username that is invalid
    @Test
    public void registerTest_BadRequest() throws dataAccess.DataAccessException {

        RegisterRequest request = new RegisterRequest(null, "password", "email");
        RegisterResult result = registerService.register(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: bad request", result.getMessage());
    }

    // Here a user is trying to register with a username that has already been taken
    @Test
    public void registerTest_Taken() throws DataAccessException, dataAccess.DataAccessException {
        User user = new User("Ishmael", "password", "italeo@gmail.com");
        userDAO.insert(user);
        RegisterRequest request = new RegisterRequest("Ishmael", "password", "italeo@gmail.com");
        RegisterResult result = registerService.register(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: already taken", result.getMessage());
    }
}
