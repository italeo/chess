import handler.*;
import spark.Spark;

public class Server {

    private void run() {
        // Sets the fixed port to 8080
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");
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