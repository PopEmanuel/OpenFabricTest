package ai.openfabric.api.handler;

import ai.openfabric.api.exception.ApiException;
import ai.openfabric.api.exception.DockerContainerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleJobFailureException(RuntimeException e) {
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, 500, ZonedDateTime.now(ZoneId.of("Z")));
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {DockerContainerException.class})
    public ResponseEntity<Object> handleInterruptedException(DockerContainerException e) {
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, 500, ZonedDateTime.now(ZoneId.of("Z")));
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}