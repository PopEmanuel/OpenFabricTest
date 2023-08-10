package ai.openfabric.api.exception;

public class DockerContainerException extends RuntimeException {
    public DockerContainerException(String message) {
        super(message);
    }
}
