package dataaccess.authdao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.util.UUID;

public class SQLAuthDao implements AuthDao{
    public AuthData createAuth(String username) throws DataAccessException {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                ps.setString(2, authData.username());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
        return authData;
    }

    public int removeAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auth WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                return ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE auth";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
