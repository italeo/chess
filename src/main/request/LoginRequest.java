package request;

/** Represents the request of the user trying to log in to the game.*/
public class LoginRequest {
    private final String username;
    private final String password;

    /** Constructs the request needed for the user to login, storing the necessary information from these parameters:
     * @param username - The username representing the user.
     * @param password - The users password.
     * */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
