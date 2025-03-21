package request;

public class JoinRequest {
    String authToken;
    int gameID;
    String playerColor;

    public JoinRequest(String auth, int game, String color) {
        this.authToken = auth;
        this.gameID = game;
        this.playerColor = color;
    }

    public void setAuthToken(String auth) {
        this.authToken = auth;
    }

    public String authToken() {
        return this.authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }
}
