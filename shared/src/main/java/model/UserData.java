package model;

import request.RegisterRequest;

public record UserData(String username, String password, String email) {
    public UserData(RegisterRequest request) {
        this(request.username(), request.password(), request.email());
    }
}
