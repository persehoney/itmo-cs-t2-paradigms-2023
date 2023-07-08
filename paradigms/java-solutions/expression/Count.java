package expression;

import expression.exceptions.OverflowException;

public class Count extends Operation implements TripleExpression {
    public Count(Operation first) {
        this.first = first;
    }

    @Override
    public int evaluate(int x, int y, int z) throws OverflowException {
        String string = Integer.toBinaryString(first.evaluate(x, y, z));
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '1') {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return "count(" + first.toString() + ")";
    }

    public boolean equals(Operation operation) {
        return operation instanceof Count &&
                this.first.equals(operation.first);
    }
}
