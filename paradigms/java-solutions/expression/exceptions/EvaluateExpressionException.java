package expression.exceptions;

public class EvaluateExpressionException extends ExpressionException {
   EvaluateExpressionException(String message) {
       super("EvaluateExpressionException: " + message);
   }
}
