package result;

public record LoginResult (String username, String authToken, String message){
    public LoginResult(String username, String authToken) {
        this(username, authToken, "");
    }
}
