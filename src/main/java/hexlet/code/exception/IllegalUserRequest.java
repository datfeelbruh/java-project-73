package hexlet.code.exception;

public class IllegalUserRequest extends RuntimeException {
    public IllegalUserRequest(String message) {
        super(message);
    }
}
