package handler;

import com.google.gson.Gson;
import request.CreateRequest;
import result.CreateResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateHandler implements Route {
    private GameService service;

    public CreateHandler(GameService gameService) {
        this.service = gameService;
    }

    public String handle(Request req, Response res) {
        var request = new Gson().fromJson(req.body() , CreateRequest.class);
        CreateResult result = service.createGame(request);
        return switch (result.message()) {
            case "Error: unauthorized" -> {
                res.status(401);
                yield "{ \"message\": \"Error: unauthorized\" }";
            }
            case "Error: bad request" -> {
                res.status(400);
                yield "{ \"message\": \"Error: bad request\" }";
            }
            case "Error: Data Access Exception" -> {
                res.status(500);
                yield "{ \"message\": \"Error: Data Access Exception\" }";
            }
            case null, default -> {
                res.status(200);
                yield new Gson().toJson(result, CreateResult.class);
            }
        };
    }
}
