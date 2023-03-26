package hexlet.code.exception;

public class CustomConstraintException extends RuntimeException {
    public CustomConstraintException(String message) {
        super(message);
    }
}
