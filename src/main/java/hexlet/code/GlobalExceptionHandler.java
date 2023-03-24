package hexlet.code;

import hexlet.code.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public final class GlobalExceptionHandler {
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler
    public ResponseEntity<RequestError> catchResourceNotFoundException(ResourceNotFoundException e) {
        EXCEPTION_LOGGER.error(e.getMessage(), e);
        return new ResponseEntity<>(
                new RequestError(
                        HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        e.getMessage()),
                        HttpStatus.UNPROCESSABLE_ENTITY
        );
    }
}
