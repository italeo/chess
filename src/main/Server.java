import handler.*;
import spark.Spark;
import java.util.ArrayList;

public class Server {
    private final ArrayList<String> names = new ArrayList<>();

    private void run() {
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");
        Spark.init();

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