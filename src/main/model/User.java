package model;

/** Sets the structure for the user in the user table in the database. */
public class User {
    /** The username representing the user in the game.*/
    private String username;
    /** The password for the user.*/
    private String password;
    /** The user's email address.*/
    private String email;

    /** Constructs the user information that goes into the database.
     * @param username - The user's name in the game.
     * @param email - The user's email address.
     * @param password - The users password to their account.
     * */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
