package expression.exceptions;

public class MissingArgumentException extends ParseExpressionException {
    public MissingArgumentException(int position) {
        super("MissingArgumentException: missing argument at " + position + " position");
    }
    public MissingArgumentException(String message) {
        super("MissingArgumentException: " + message);
    }
}
