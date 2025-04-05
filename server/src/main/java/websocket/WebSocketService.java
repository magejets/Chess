package websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.gamedao.GameDao;
import model.AuthData;
import model.GameData;

public class WebSocketService {
    private AuthDao authDao;
    private GameDao gameDao;

    public WebSocketService(AuthDao authDao, GameDao gameDao) {
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public GameData makeMove(int gameID, ChessMove move, ChessGame.TeamColor turn) throws Exception{
        try {
            ChessGame game = gameDao.getGame(gameID).getGame();
            if (game.getBoard().getPiece(move.getStartPosition()).getTeamColor() == game.getTeamTurn() &&
                turn == game.getTeamTurn()) {
                game.setBoard(game.getBoard().makeMove(move));
                ChessGame.TeamColor nextTurn = game.getTeamTurn() == ChessGame.TeamColor.WHITE ?
                        ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
                game.setTeamTurn(nextTurn);
                gameDao.updateGame(gameID, game);
                return gameDao.getGame(gameID);
            } else {
                throw new Exception("Wrong turn");
            }
        } catch (DataAccessException e) {
            throw new Exception(e.getMessage());
        }
    }

    public String getUsername(String authToken) throws Exception{
        try {
            AuthData userAuth = authDao.getAuth(authToken);
            if (userAuth != null) {
                return userAuth.username();
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            throw new Exception("Data Access Error");
        }
    }

    public ChessGame.TeamColor getTurn(String userName, int gameID) throws Exception{
        try {
            GameData game = gameDao.getGame(gameID);
            if (userName.equals(game.getWhiteUsername())) {
                return ChessGame.TeamColor.WHITE;
            } else if (userName.equals(game.getBlackUsername())) {
                return ChessGame.TeamColor.BLACK;
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            throw new Exception("Bad ID");
        }
    }

    public GameData getGame(int gameID) {
        try {
            return gameDao.getGame(gameID);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public boolean validateAuth(String authToken) {
        try {
            AuthData checkData = authDao.getAuth(authToken);
            return checkData != null;
        } catch (DataAccessException e) {
            return false;
        }
    }
}
