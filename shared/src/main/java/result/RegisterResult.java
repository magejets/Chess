package result;


public class RegisterResult{
    private String username;
    private String authToken;
    transient private String message;

    public RegisterResult(String username, String authToken, String message){
        this.username = username;
        this.authToken = authToken;
        this.message = message;
    }
    public RegisterResult(LoginResult result) {
        this(result.username(), result.authToken(), result.message());
    }
    public RegisterResult(String message) {
        this("", "", message);
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
