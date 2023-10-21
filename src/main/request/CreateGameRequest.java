package request;

/** Represents the request to create a new game. */
public class CreateGameRequest {

    private String game;



    public CreateGameRequest(String game) {
       this.game = game;
    }

    public String getGame() {
        return game;
    }
}
