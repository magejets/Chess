package dataaccess.gamedao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDao {
    public List<GameData> getGames() throws DataAccessException;
    public int createGame(GameData game) throws DataAccessException;
    public GameData getGame(Integer gameID) throws DataAccessException;
    public int updateGame(int gameID, String playerColor, String username) throws DataAccessException;
    public int updateGame(int gameID, ChessGame game) throws DataAccessException;
    public int updateGame(int gameID, GameData game) throws DataAccessException;
    public void clear() throws DataAccessException;
}
