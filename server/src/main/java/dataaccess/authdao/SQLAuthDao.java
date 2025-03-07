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
        // INSERT INTO auth (authToken, username) VALUES (@authToken, @username)
    }
    public void removeAuth(String authToken) throws DataAccessException {
        // DELETE FROM auth WHERE authToken = @authToken
    }
    public AuthData getAuth(String authToken) throws DataAccessException {
        return new AuthData();
        // SELECT authToken, username FROM auth WHERE authToken = @authToken
    }
    public void clear() throws DataAccessException {
        // DELETE FROM auth
    }
}
