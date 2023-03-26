package hexlet.code.exception;

public class CustomAuthorizationException extends RuntimeException {
    public CustomAuthorizationException(String message) {
        super(message);
    }
}
