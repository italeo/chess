package passoffTests.serviceTests;

import dao.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    private AuthTokenDAO authTokenDAO;
    private UserDAO userDAO;
    private RegisterService registerService;

    @BeforeEach
    public void setUp() {
        authTokenDAO = new AuthTokenDAO();
        userDAO = new UserDAO();
        registerService = new RegisterService(authTokenDAO, userDAO);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        userDAO.clear();
        authTokenDAO.clear();
    }

    @Test
    public void registerTest_Success() {
        RegisterRequest request = new RegisterRequest("Ishmael", "password", "email");
        RegisterResult result = registerService.register(request);

        assertTrue(result.isSuccess());
        assertEquals(request.getUsername(), result.getUsername());
        assertNotNull(result.getAuthToken());
    }

    @Test
    public void registerTest_BadRequest() {

        RegisterRequest request = new RegisterRequest(null, "password", "email");
        RegisterResult result = registerService.register(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: bad request", result.getMessage());
    }

    @Test
    public void registerTest_Taken() throws DataAccessException {
        User user = new User("Ishmael", "password", "italeo@gmail.com");
        userDAO.insert(user);
        RegisterRequest request = new RegisterRequest("Ishmael", "password", "italeo@gmail.com");
        RegisterResult result = registerService.register(request);

        assertFalse(result.isSuccess());
        assertEquals("Error: already taken", result.getMessage());
    }
}
