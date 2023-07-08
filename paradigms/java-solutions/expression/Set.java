package expression;

public class Set extends Operation implements TripleExpression {
    public Set(Operation first, Operation second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int arg1 = first.evaluate(x, y, z);
        int arg2 = 1 << second.evaluate(x, y, z);
        return arg1 | arg2;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + " set " + second.toString() + ")";
    }

    public boolean equals(Operation operation) {
        return operation instanceof Set &&
                this.first.equals(operation.first) &&
                this.second.equals(operation.second);
    }
}
