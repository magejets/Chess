package result;


public record RegisterResult (String username, String authToken){
    public RegisterResult(LoginResult result) {
        this(result.username(), result.authToken());
    }
}
