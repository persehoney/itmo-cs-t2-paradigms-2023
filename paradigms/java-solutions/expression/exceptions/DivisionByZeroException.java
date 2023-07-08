package expression.exceptions;

public class DivisionByZeroException extends EvaluateExpressionException {
    public DivisionByZeroException() {
        super("division by zero!");
    }

}
