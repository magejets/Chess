package handler;

import com.google.gson.Gson;
import request.CreateRequest;
import request.ListRequest;
import result.ListResult;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListHandler implements Route {
    private GameService service;

    public ListHandler(GameService gameService) {
        this.service = gameService;
    }

    public String handle(Request req, Response res) {
//        System.out.println("The straight dope: " + req.body());
//        var request = new Gson().fromJson(req.body() , ListRequest.class);
//        ListResult result = service.listGames(request);
        var request = new ListRequest(req.headers("Authorization"));
        ListResult result = service.listGames(request);
        return switch (result.getMessage()) {
            case "Error: unauthorized" -> {
                res.status(401);
                yield "{ \"message\": \"Error: unauthorized\" }";
            }
            case "Error: Data Access Exception" -> {
                res.status(500);
                yield "{ \"message\": \"Error: Data Access Exception\" }";
            }
            case null, default -> {
                res.status(200);
                yield new Gson().toJson(result, ListResult.class);
            }
        };

    }
}
