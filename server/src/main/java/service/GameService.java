package service;

import dataaccess.DataAccessException;
import dataaccess.gamedao.MemoryGameDao;
import model.AuthData;
import model.GameData;
import request.CreateRequest;
import request.JoinRequest;
import request.ListRequest;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;

import java.util.List;

public class GameService extends Service{
    final private MemoryGameDao dataAccess = new MemoryGameDao();

    public ListResult listGames(ListRequest request) {
        try {
            if (authorize(request.authToken()) == null) {
                return new ListResult("Error: unauthorized");
            }
            List<GameData> gameList;
            gameList = dataAccess.getGames();

            return new ListResult(gameList);
        } catch (DataAccessException e) {
            return new ListResult(e.getMessage());
        }
    }

    public CreateResult createGame(CreateRequest request) {
        try {
            if (authorize(request.getAuthToken()) == null) {
                return new CreateResult("Error: unauthorized");
            }
            if (request.getGameName().isEmpty()) {
                return new CreateResult("Error: bad request");
            }
            GameData game = new GameData(request.getGameName());
            int gameID;
            gameID = dataAccess.createGame(game);

            return new CreateResult(gameID);
        } catch (DataAccessException e) {
            return new CreateResult(e.getMessage());
        }
    }

    public JoinResult joinGames(JoinRequest request) {
        AuthData authData;
        try {
            authData = authorize(request.getAuthToken());
            if (authData == null) {
                return new JoinResult("Error: unauthorized");
            } else {
                if (request.getGameID() == null || request.getGameID() <= 0) {
                    return new JoinResult("Error: bad request");
                }

                    if (request.getPlayerColor() != null &&
                            (request.getPlayerColor().equals("WHITE") || request.getPlayerColor().equals("BLACK"))) {
                        boolean notTaken = dataAccess.updateGame(request.getGameID(), request.getPlayerColor(), authData.username());
                        if (!notTaken) {
                            return new JoinResult("Error: already taken");
                        }
                    } else {
                        return new JoinResult("Error: bad request");
                    }

            }
        } catch (DataAccessException e) {
            return new JoinResult(e.getMessage());
        }

        return new JoinResult("");
    }
}
