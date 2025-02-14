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
        Spark.post("/user", this::register); // Register
        Spark.post("/session", this::login); // Login
        Spark.delete("/session", this::logout); // Logout

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();



        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) { // throws ResponseException {
        var handler = new RegisterHandler(req);
        String response = handler.register(); // figure out what datatype this needs to be
        res.status(200);
        return response;
    }

    private Object login(Request req, Response res) {
        var handler = new LoginHandler(req);
        String response = handler.login();
        res.status(200);
        return response;
    }

    private Object logout(Request req, Response res) {
        var handler = new LogoutHandler(req);
        String response = handler.logout();
        res.status(200);
        return response;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
