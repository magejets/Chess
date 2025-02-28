package handler;

import com.google.gson.Gson;
import request.ListRequest;
import result.ListResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListHandler implements Route {
    public String handle(Request req, Response res) {
        var request = new ListRequest(req.headers("Authorization"));
        var service = new GameService();
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
                yield new Gson().toJson(service.listGames(request), ListResult.class);
            }
        };

    }
}
