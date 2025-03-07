package server;

//import exception.ResponseException;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.SQLAuthDao;
import dataaccess.gamedao.GameDao;
import dataaccess.gamedao.SQLGameDao;
import dataaccess.userdao.SQLUserDao;
import dataaccess.userdao.UserDao;
import model.GameData;
import service.ClearService;
import service.GameService;
import service.UserService;
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
        // Create the data access classes
        UserDao userDao = new SQLUserDao(); // the configureDatabase is called in this constructor
        AuthDao authDao = new SQLAuthDao();
        GameDao gameDao = new SQLGameDao();

        // Create the services
        UserService  userService  = new UserService(userDao, authDao);
        GameService  gameService  = new GameService(authDao, gameDao);
        ClearService clearService = new ClearService(userDao, authDao, gameDao);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", new RegisterHandler(userService)); // Register
        Spark.post("/session", new LoginHandler(userService)); // Login
        Spark.delete("/session", new LogoutHandler(userService)); // Logout // check if it needs user tambien
        Spark.get("/game", new ListHandler(gameService)); // List Games
        Spark.post("/game", new CreateHandler(gameService)); // Create Games
        Spark.put("/game", new JoinHandler(gameService)); // Join Games
        Spark.delete("/db", new ClearHandler(clearService));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}