package expression;

public class Clear extends Operation implements TripleExpression {
    public Clear(Operation first, Operation second) {
        super(first, second);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int arg1 = first.evaluate(x, y, z);
        int arg2 = 1 << second.evaluate(x, y, z);
        return arg1 & ~arg2;
    }

    @Override
    public String toString() {
        return "(" + first.toString() + " clear " + second.toString() + ")";
    }

    public boolean equals(Operation operation) {
        return operation instanceof Clear &&
                this.first.equals(operation.first) &&
                this.second.equals(operation.second);
    }
}
