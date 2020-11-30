package in.athenaeum.demoexceptionsdb.exceptions;

public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException(String message) {
        super(message);
    }

    public AlreadyDeletedException() {
    }
}
