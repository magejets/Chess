package result;

public class CreateResult {
    int gameID;
    transient String message;

    public CreateResult(int gameID, String message) {
        this.gameID = gameID;
        this.message = message;
    }
    public CreateResult(int gameID) {
        this(gameID, "");
    }

    public CreateResult(String message) {
        this(-1, message);
    }

    public int gameID() {
        return this.gameID;
    }

    public String message() {
        return this.message;
    }
}
