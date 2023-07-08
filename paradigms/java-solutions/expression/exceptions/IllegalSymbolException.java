package expression.exceptions;

public class IllegalSymbolException extends ParseExpressionException {
    public IllegalSymbolException(int position) {
        super("IllegalSymbolException: unexpected character at " + position + " position");
    }
}
