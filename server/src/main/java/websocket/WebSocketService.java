package websocket;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.DataAccessException;
import dataaccess.authdao.AuthDao;
import dataaccess.gamedao.GameDao;
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
            return authDao.getAuth(authToken).username();
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
}
