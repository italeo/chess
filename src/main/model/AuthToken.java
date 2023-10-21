package model;

import java.util.Objects;

/** Sets the structure for the AuthToken table in the database*/
public class AuthToken {
    /** The authorization token of a signed-in user. */
    private String authToken;
    /** The user's username, used by other players to identify the user. */
    private String username;

    /** Constructs the authToken object the user gets when logged into the game
     * @param authToken - The unique token given to the user.
     * @param username - The username associate with the user.
     * */
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

}
