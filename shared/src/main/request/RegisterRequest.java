package request;

/** Represents the request object for creating a new registration for a new user. */
public class RegisterRequest {
    /** The user's identifier in the game. */
    private final String username;
    /** The user's password used to log into the game. */
    private final String password;
    /** The user's email address. */
    private final String email;

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

}
