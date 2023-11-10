package model;

/** Sets the structure for the AuthToken table in the database*/
public class AuthToken {
    /** The authorization token of a signed-in user. */
    private String authToken;
    /** The user's username, used by other players to identify the user. */
    private String username;

    public AuthToken() {
    }

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

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
