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

    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        logoutService = new LogoutService(authTokenDAO);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
    }

    @Test
    public void logoutTest_Success() throws DataAccessException{
        AuthToken authToken = new AuthToken("validToken", "Ishmael");
        authTokenDAO.insert(authToken);

        LogoutRequest request = new LogoutRequest("validToken");
        LogoutResult result = logoutService.logout(request);

        assertTrue(result.isSuccess());
    }

    @Test
    public void logoutTest_Unauthorized() throws DataAccessException{
        AuthToken authToken = new AuthToken("validToken", "Ishmael");
        authTokenDAO.insert(authToken);

        LogoutRequest request = new LogoutRequest("invalidToken");
        LogoutResult result = logoutService.logout(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: unauthorized", result.getMessage());
    }
}
