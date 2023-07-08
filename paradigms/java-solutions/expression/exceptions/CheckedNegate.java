package expression.exceptions;

import expression.Operation;
import expression.TripleExpression;

public class CheckedNegate extends Operation implements TripleExpression {
    String type;
    public CheckedNegate(Operation first) {
        this.first = first;
    }
    public CheckedNegate(Operation first, String type) {
        this.first = first;
        this.type = type;
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException {
        int arg = first.evaluate(x, y, z);
        if (arg == Integer.MIN_VALUE) {
            throw new OverflowException(arg);
        }
        return - arg;
    }

    @Override
    public String toString() {
        if (type != null && type.equals("brackets"))
            return "-(" + first.toString() + ")";
        return "-" + first.toString();
    }

    public boolean equals(Operation operation) {
        return operation instanceof CheckedNegate &&
                this.first.equals(operation.first);
    }
}