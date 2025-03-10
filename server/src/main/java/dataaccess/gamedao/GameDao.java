package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDao {
    public List<GameData> getGames() throws DataAccessException;
    public int createGame(GameData game) throws DataAccessException;
    public GameData getGame(Integer gameID) throws DataAccessException;
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException;
    public void clear() throws DataAccessException;
}
