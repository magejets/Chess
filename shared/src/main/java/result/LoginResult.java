package result;

public class LoginResult{
    String username;
    String authToken;
    transient String message;

    public LoginResult(String username, String authToken, String message){
        this.username = username;
        this.authToken = authToken;
        this.message = message;
    }
    public LoginResult(String username, String authToken) {
        this(username, authToken, "");
    }

    public String username() {
        return this.username;
    }

    public String authToken() {
        return this.authToken;
    }

    public String message() {
        return this.message;
    }
}
