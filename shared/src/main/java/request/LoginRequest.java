package request;

public record LoginRequest (String username, String password){
    public LoginRequest(RegisterRequest request) {
        this(request.username(), request.password());
    }
}
