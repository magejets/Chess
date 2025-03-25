package service;

import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.authdao.MemoryAuthDao;
import dataaccess.authdao.SQLAuthDao;
import dataaccess.gamedao.GameDao;
import dataaccess.gamedao.MemoryGameDao;
import dataaccess.gamedao.SQLGameDao;
import dataaccess.userdao.SQLUserDao;
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
    private GameDao dataAccess;

    public GameService(AuthDao authDao, GameDao gameDao) {
        super(authDao);
        this.dataAccess = gameDao;
    }

    // the following constructor is for the tests only and when NOT working with http
    public GameService() {
        super(new SQLAuthDao());
        this.dataAccess = new SQLGameDao();
    }

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
            if (authorize(request.authToken()) == null) {
                return new CreateResult("Error: unauthorized");
            }
            if (request.getGameName() == null || request.getGameName().isEmpty()) {
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
            authData = authorize(request.authToken());
            if (authData == null) {
                return new JoinResult("Error: unauthorized");
            } else {
                if (request.getGameID() == null || request.getGameID() <= 0) {
                    return new JoinResult("Error: bad request");
                }

                    if (request.getPlayerColor() != null &&
                            (request.getPlayerColor().equals("WHITE") || request.getPlayerColor().equals("BLACK"))) {

                        GameData oldGame = dataAccess.getGame(request.getGameID());
                        if (oldGame == null) {
                            return new JoinResult("Error: game does not exist");
                        }
                        if (request.getPlayerColor().equals("WHITE") ?
                                (oldGame.getWhiteUsername() == null || oldGame.getWhiteUsername().isEmpty()) :
                                (oldGame.getBlackUsername() == null || oldGame.getBlackUsername().isEmpty())) {
                           dataAccess.updateGame(request.getGameID(), request.getPlayerColor(), authData.username());
                        } else {
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
