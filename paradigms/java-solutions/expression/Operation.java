package expression;

public class Operation implements TripleExpression {
    public Operation first;
    public Operation second;

    public Operation(Operation first, Operation second) {
        this.first = first;
        this.second = second;
    }

    public Operation() {
    }
    public int evaluate(int x, int y, int z) {
        return first.evaluate(x, y, z) + second.evaluate(x, y, z);
    }
}
