package exceptions;

public class AuthException extends GameException {
    private String username;

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, String username) {
        super(message);
        this.username = username;
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthException(String message, String username, Throwable cause) {
        super(message, cause);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
