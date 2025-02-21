package server;

//import exception.ResponseException;
import model.GameData;
import spark.*;
import handler.*;
import com.google.gson.Gson;

import java.util.List;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");// change to just "web" if doesn't work

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register); // Register
        Spark.post("/session", this::login); // Login
        Spark.delete("/session", this::logout); // Logout // debug
        Spark.get("/game", this::listGames); // List Games // same header issue as with logout

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request req, Response res) { // throws ResponseException {
        var handler = new RegisterHandler(req);
        String response = handler.register();
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

    private Object listGames(Request req, Response res) {
        var handler = new ListHandler(req);
        String response = handler.listGames(); // should this be a list of games?
        res.status(200);
        return response;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
