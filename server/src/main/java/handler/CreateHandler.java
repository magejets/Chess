package handler;

import com.google.gson.Gson;
import request.CreateRequest;
import result.CreateResult;
import result.JoinResult;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateHandler implements Route {
    public String handle(Request req, Response res) {
        var request = new Gson().fromJson(req.body() , CreateRequest.class);
        request.setAuthToken(req.headers("Authorization"));
        var service = new GameService();
        CreateResult result = service.createGame(request);
        switch (result.message()) {
            case "Error: unauthorized":
                res.status(401);
                return "{ \"message\": \"Error: unauthorized\" }";
            case "Error: bad request":
                res.status(400);
                return "{ \"message\": \"Error: bad request\" }";
            case "Error: Data Access Exception":
                res.status(500);
                return "{ \"message\": \"Error: Data Access Exception\" }";
            case null, default:
                res.status(200);
                return new Gson().toJson(result, CreateResult.class);
        }
    }
}
