package expression.exceptions;

public class ParseExpressionException extends ExpressionException {
    ParseExpressionException(String message) {
        super("ParseExpressionException: " + message);
    }
}
