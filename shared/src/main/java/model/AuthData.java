package model;

public record AuthData(String authToken, String username) {
    public AuthData() {
        this("", "");
    }
}