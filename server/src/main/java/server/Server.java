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

        createRoutes();

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private static void createRoutes() {
        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new RegisterHandler()); // Register
        Spark.post("/session", new LoginHandler()); // Login
        Spark.delete("/session", new LogoutHandler()); // Logout
        Spark.get("/game", new ListHandler()); // List Games
        Spark.post("/game", new CreateHandler()); // Create Games
        Spark.put("/game", new JoinHandler()); // Join Games
        Spark.delete("/db", new ClearHandler());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}