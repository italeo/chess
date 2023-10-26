package passoffTests.serviceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private AuthTokenDAO authTokenDAO;
    private UserDAO userDAO;
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        userDAO = new UserDAO();
        loginService = new LoginService(authTokenDAO, userDAO);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        authTokenDAO.clear();
        userDAO.clear();
    }

    @Test
    public void loginTest_Success() throws DataAccessException {
        User user = new User("Ishmael", "password", "it@gmail.com");
        userDAO.insert(user);

        LoginRequest request = new LoginRequest("Ishmael", "password");
        LoginResult result = loginService.login(request);

        assertTrue(result.isSuccess());
        assertEquals(request.getUsername(), result.getUsername());
        assertNotNull(result.getAuthToken());
    }

    @Test
    public void loginTest_Unauthorized() throws DataAccessException {
        User user = new User("Ishmael", "password", "it@gmail.com");
        userDAO.insert(user);

        LoginRequest request = new LoginRequest(null, "password");
        LoginResult result = loginService.login(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: unauthorized", result.getMessage());

    }
}
