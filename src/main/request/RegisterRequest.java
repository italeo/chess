package request;

/** Represents the request object for creating a new registration for a new user. */
public class RegisterRequest {
    private String username;
    private String password;
    private String email;

    /** Constructs the registration request from the new user.
     * @param username - The username the user wishes to use.
     * @param password - The password the user needs to log in into the game.
     * @param email - Needed for the user to register.
     * */
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
