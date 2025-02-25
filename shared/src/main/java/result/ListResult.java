package result;

import model.GameData;

import java.util.List;

public class ListResult {
    List<GameData> games;
    public ListResult(List<GameData> gameList) {
        this.games = gameList;
    }
}
