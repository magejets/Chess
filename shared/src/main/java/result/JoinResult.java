package result;

public class JoinResult {
    transient private String message;

    public JoinResult(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
