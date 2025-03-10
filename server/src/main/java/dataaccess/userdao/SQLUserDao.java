package dataaccess.userdao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class SQLUserDao implements UserDao{
    public SQLUserDao() {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            assert false;
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT id, username, password, email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }
    public void createUser(UserData newUser) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT into user (username, password, email) VALUES (?, ?, ?)";
            try (var ps = conn.prepareStatement(statement)) {
                String hashedPassword = BCrypt.hashpw(newUser.password(), BCrypt.gensalt());
                ps.setString(1, newUser.username());
                ps.setString(2, hashedPassword);
                ps.setString(3, newUser.email());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE user";
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
