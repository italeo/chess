package request;

/** Represents the request of the user trying to log in to the game.*/
public class LoginRequest {
    private String username;
    private String password;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
