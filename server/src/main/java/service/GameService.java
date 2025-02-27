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

import java.util.ArrayList;
import java.util.List;

public class GameService extends Service{
    final private MemoryGameDao dataAccess = new MemoryGameDao();

    public ListResult listGames(ListRequest request) {
        // authorize first
        try {
            if (authorize(request.authToken()) != null) {
                List<GameData> gameList = new ArrayList<>();
                try {
                    gameList = dataAccess.getGames();
                } catch (DataAccessException e) {
                    return new ListResult("Error: Data Access Exception");
                }

                return new ListResult(gameList);
            } else {
                return new ListResult("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            return new ListResult("Error: Data Access Exception");
        }
    }

    public CreateResult createGame(CreateRequest request) {
        // authorize first
        try {
            if (authorize(request.getAuthToken()) != null) {
                GameData game = new GameData(request.getGameName());
                int gameID = -1; // initialized to a value it will never naturally be
                try {
                    gameID = dataAccess.createGame(game);
                } catch (DataAccessException e) {
                    return new CreateResult("Error: Data Access Exception");
                }

                return new CreateResult(gameID);
            } else {
                return new CreateResult("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            return new CreateResult("Error: Data Access Exception");
        }
    }

    public JoinResult joinGames(JoinRequest request) {
        // authorize first
        AuthData authData;
        try {
            authData = authorize(request.getAuthToken());
        } catch (DataAccessException e) {
            return new JoinResult("Error: Data Access Exception");
        }
        if (authData == null) {
            return new JoinResult("Error: unauthorized");
        } else {
            try {
                if (request.getPlayerColor().equals("WHITE") || request.getPlayerColor().equals("BLACK")) {
                    boolean notTaken = dataAccess.updateGame(request.getGameID(), request.getPlayerColor(), authData.username());
                    if (!notTaken) {
                        return new JoinResult("Error: already taken");
                    }
                } else {
                    return new JoinResult("Error: bad request");
                }
            } catch (DataAccessException e) {
                return new JoinResult("Error: Data Access Exception");
            }
        }

        return new JoinResult("");
    }
}
