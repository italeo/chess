package passoffTests.serviceTests;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LogoutRequest;
import result.LogoutResult;
import service.LogoutService;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTest {
    private AuthTokenDAO authTokenDAO;
    private LogoutService logoutService;

    // Setting for the test by creating the db, and needed components for testing
    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        logoutService = new LogoutService(authTokenDAO);
    }

    // Making sure we reset everything after every tests
    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
    }

    // To logout, we need a valid token so that is created and inserted into the db so that we are able to validate the user
    // and log them out
    @Test
    public void logoutTest_Success() throws DataAccessException, dataAccess.DataAccessException {
        AuthToken authToken = new AuthToken("validToken", "Ishmael");
        authTokenDAO.insert(authToken);

        LogoutRequest request = new LogoutRequest("validToken");
        LogoutResult result = logoutService.logout(request);

        assertTrue(result.isSuccess());
    }

    // This check if a not valid authToken is trying to log out
    @Test
    public void logoutTest_Unauthorized() throws DataAccessException, dataAccess.DataAccessException {
        AuthToken authToken = new AuthToken("validToken", "Ishmael");
        authTokenDAO.insert(authToken);

        LogoutRequest request = new LogoutRequest("invalidToken");
        LogoutResult result = logoutService.logout(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: unauthorized", result.getMessage());
    }
}
