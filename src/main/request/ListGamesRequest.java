package request;

/** Represents the request for listing all available chess games.
 * */

public class ListGamesRequest {
    /** The unique token assigned to the user during log in or registration. */
    private String authToken;

    /** Constructs the request for the list of all games the user could join
     * @param authToken - The unique token used to authorize the user to access the list*/
    public ListGamesRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
