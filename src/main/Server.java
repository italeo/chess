import handler.ClearHandler;
import result.ClearResult;
import spark.Spark;

import java.util.ArrayList;

public class Server {
    private ArrayList<String> names = new ArrayList<>();

    private void run() {
        //Port that server will listen on
        Spark.port(8080);
        Spark.externalStaticFileLocation("web");
        Spark.init();

        Spark.delete("/db", (request, result) -> {
            ClearHandler clearHandler = new ClearHandler();
            return null;
        });
    }
}