package dataaccess.authdao;

import model.AuthData;

public interface AuthDao {
    public AuthData createAuth(String username);
    public void removeAuth(String authToken);
    public AuthData getAuth(String authToken);
}
