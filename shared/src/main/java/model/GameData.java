package model;

import chess.ChessGame;

import java.util.Objects;

public class GameData {
    Integer gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    transient ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        setGameID(gameID);
        setWhiteUsername(whiteUsername);
        setBlackUsername(blackUsername);
        setGameName(gameName);
        this.game = game;
    }

    public GameData(String gameName) {
        setGameID(-1);
        setWhiteUsername(null);
        setBlackUsername(null);
        setGameName(gameName);
        setGame(new ChessGame());
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameData gameData = (GameData) o;
        return getGameID() == gameData.getGameID() && Objects.equals(getWhiteUsername(), gameData.getWhiteUsername()) &&
                Objects.equals(getBlackUsername(), gameData.getBlackUsername()) && Objects.equals(getGameName(),
                gameData.getGameName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameID(), getWhiteUsername(), getBlackUsername(), getGameName(), getGame());
    }
}
