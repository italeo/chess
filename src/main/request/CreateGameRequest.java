package request;

/** Represents the request to create a new game. */
public class CreateGameRequest {
    private String player1;
    private String player2;


    /** Constructs a CreateGameRequest with defined players.
     * @param player1 - the first player.
     * @param  player2 - The second player.
     * */
    public CreateGameRequest(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }


    // -------------------------- MIGHT NOT NEED SETTERS SO COE BACK TO IT ---------------------------------
    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }
}
