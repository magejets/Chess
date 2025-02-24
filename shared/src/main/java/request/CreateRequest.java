package request;

public class CreateRequest {
    String authToken;
    String gameName;

    public CreateRequest(String auth, String game) {
        this.authToken = auth;
        this.gameName = game;
    }

    public void setAuthToken(String auth) {
        this.authToken = auth;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setGameName(String game) {
        this.gameName = game;
    }

    public String getGameName() {
        return this.gameName;
    }
}
