package handler;

import com.google.gson.Gson;
import result.ClearResult;
import service.ClearService;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    private ClearService service;

    public ClearHandler(ClearService clearService) {
        this.service = clearService;
    }

    public String handle(Request req, Response res) {
        ClearResult result = service.clear();
        return switch (result.message()) {
            case "" -> {
                res.status(200);
                yield "{}";
            }
            case null, default -> {
                res.status(500);
                yield new Gson().toJson(service.clear(), ClearResult.class);
            }
        };
    }
}
