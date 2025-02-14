package server;

//import exception.ResponseException;
import spark.*;
import handler.*;
import com.google.gson.Gson;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/user", this::register);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) { // throws ResponseException {
        var handler = new RegisterHandler(req);
        String response = handler.register(); // figure out what datatype this needs to be
        res.status(200);
        return response;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
