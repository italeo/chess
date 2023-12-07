import handler.*;
import handler.webSocket.WebSocketHandler;
import spark.Spark;

public class Server {
// fixme: Set up the webSocket portion of the Server
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();

    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");

        Spark.webSocket("/connect", webSocketHandler);

        Spark.init();

        // Paths for each requests
        Spark.delete("/db", new ClearHandler());
        Spark.post("/game", new CreateGameHandler());
        Spark.put("/game", new JoinGameHandler());
        Spark.get("/game", new ListGameHandler());
        Spark.post("/session", new LoginHandler());
        Spark.delete("/session", new LogoutHandler());
        Spark.post("/user",  new RegisterHandler());
    }

    public static void main(String[] args) {
        new Server().run();
    }
}