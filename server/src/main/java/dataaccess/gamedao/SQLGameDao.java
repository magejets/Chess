package dataaccess.gamedao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.util.ArrayList;
import java.util.List;


public class SQLGameDao implements GameDao{
    public List<GameData> getGames() throws DataAccessException {
        ArrayList<GameData> result = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(new GameData(rs.getInt("id"), rs.getString("whiteUsername"), rs.getString("blackUsername"),
                                rs.getString("gameName"), new Gson().fromJson(rs.getString("game"), ChessGame.class)));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public int createGame(GameData game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT into game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, game.getWhiteUsername());
                ps.setString(2, game.getBlackUsername());
                ps.setString(3, game.getGameName());
                ps.setString(4, new Gson().toJson(game.getGame(), ChessGame.class));
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return 0;
    }

    public GameData getGame(Integer gameID) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, whiteUsername, blackUsername, gameName, game FROM game WHERE id=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(rs.getInt("id"), rs.getString("whiteUsername"), rs.getString("blackUsername"),
                                rs.getString("gameName"), new Gson().fromJson(rs.getString("game"), ChessGame.class));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public int updateGame(int gameID, String playerColor, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement;
            if (playerColor.equals("WHITE")) {
                statement = "UPDATE game SET whiteUsername = ? WHERE id = ?;";
            } else if (playerColor.equals("BLACK")) {
                statement = "UPDATE game SET blackUsername = ? WHERE id = ?;";
            } else {
                throw new DataAccessException("illegal color");
            }
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setInt(2, gameID);
                return ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public int updateGame(int gameID, ChessGame game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement;
            String jsonGame = new Gson().toJson(game, ChessGame.class);
            statement = "UPDATE game SET game = ? WHERE id = ?;";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, jsonGame);
                ps.setInt(2, gameID);
                return ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE game";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
