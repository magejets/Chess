package model;

import chess.ChessGame;
import request.CreateRequest;

public class GameData {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        setGameID(gameID);
        setWhiteUsername(whiteUsername);
        setBlackUsername(blackUsername);
        setGameName(gameName);
        this.game = game;
    }

    public GameData(String gameName) {
        setGameID(-1);
        setWhiteUsername("");
        setBlackUsername("");
        setGameName(gameName);
        this.game = null;
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
}
