package expression.exceptions;

public class NoSuchModeException extends RuntimeException {
    public NoSuchModeException(String mode) {
        super("no such mode: " + mode);
    }
}
