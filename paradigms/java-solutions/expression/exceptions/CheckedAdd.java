package expression.exceptions;

import expression.Operation;
import expression.TripleExpression;

public class CheckedAdd extends Operation implements TripleExpression {
    public CheckedAdd(Operation first, Operation second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException {
        int arg1 = first.evaluate(x, y, z);
        int arg2 = second.evaluate(x, y, z);
        if (arg1 < 0 && arg2 < 0 && arg2 < Integer.MIN_VALUE - arg1
                || arg1 > 0 && arg2 > 0 && arg2 > Integer.MAX_VALUE - arg1) {
            throw new OverflowException(arg1, arg2, '+');
        }
        return arg1 + arg2;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + " + " + second.toString() + ")";
    }

    public boolean equals(Operation operation) {
        return operation instanceof CheckedAdd &&
                this.first.equals(operation.first) &&
                this.second.equals(operation.second);
    }
}
