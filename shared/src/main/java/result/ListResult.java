package result;

import model.GameData;

import java.util.List;

public class ListResult {
    private final List<GameData> games;
    private final transient String message;

    public ListResult(List<GameData> gameList) {
        this.games = gameList;
        message = "";
    }
    public ListResult(String message) {
        this.games = null;
        this.message = message;
    }

    public List<GameData> games() {
        return this.games;
    }

    public String getMessage() {
        return this.message;
    }
}
