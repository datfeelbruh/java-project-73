package hexlet.code;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestError {
    private int code;
    private String message;

    public RequestError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
