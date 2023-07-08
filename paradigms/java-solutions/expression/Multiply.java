package expression;

public class Multiply extends Operation implements TripleExpression {
    public Multiply(Operation first, Operation second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int arg1 = first.evaluate(x, y, z);
        int arg2 = second.evaluate(x, y, z);
        return arg1 * arg2;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + " * " + second.toString() + ")";
    }

    public boolean equals(Operation operation) {
        return operation instanceof Multiply &&
                this.first.equals(operation.first) &&
                this.second.equals(operation.second);
    }
}
