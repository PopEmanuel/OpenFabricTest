package ai.openfabric.api.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
public class ApiException {

    private final String message;
    private final HttpStatus httpStatus;
    private final int httpCode;
    private final ZonedDateTime timestamp;

    public ApiException(String message, HttpStatus httpStatus, int httpCode1, ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.httpCode = httpCode1;
        this.timestamp = timestamp;
    }
}