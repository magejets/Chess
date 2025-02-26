package result;


public record RegisterResult (String username, String authToken, String message){
    public RegisterResult(LoginResult result) {
        this(result.username(), result.authToken(), result.message());
    }
}
