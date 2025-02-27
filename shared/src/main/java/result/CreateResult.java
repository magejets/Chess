package result;

public record CreateResult (int gameID, String message){
    public CreateResult(int gameID) {
        this(gameID, "");
    }

    public CreateResult(String message) {
        this(-1, message);
    }
}
