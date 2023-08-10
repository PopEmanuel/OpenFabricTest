package ai.openfabric.api.exception;

public class CrudOperationException extends RuntimeException {
    public CrudOperationException(String message) {
        super(message);
    }
}
