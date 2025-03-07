package dataaccess.gamedao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class SQLGameDao implements GameDao{
    public List<GameData> getGames() throws DataAccessException {
        return new ArrayList<GameData>();
        // SELECT * FROM game
    }
    public int createGame(GameData game) throws DataAccessException {
        return 0;
        // INSERT INTO game (*) VALUES (*)
    }
    public GameData getGame(Integer gameID) {
        return new GameData("dummy");
        // SELECT * FROM games WHERE ID = @gameID
    }
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException {
        // UPDATE game SET whiteusername/blackusername = @username WHERE ID = @gameID
    }
    public void clear() throws DataAccessException {

    }
}
