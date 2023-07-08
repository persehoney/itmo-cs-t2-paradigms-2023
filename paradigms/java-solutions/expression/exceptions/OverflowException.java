package expression.exceptions;

import java.math.BigInteger;
public class OverflowException extends EvaluateExpressionException {
    public OverflowException(int arg1, int arg2, char operation) {
        super("OverflowException: " + arg1 + " " + operation + " " + arg2);
    }
    public OverflowException(int arg) {
        super("OverflowException: -" + arg);
    }
    public OverflowException(String arg) {
        super("OverflowException: " + arg + " can't be parsed as int");
    }
    public OverflowException(BigInteger arg1, BigInteger arg2, char operation) {
        super("OverflowException: " + arg1 + " " + operation + " " + arg2);
    }
}
