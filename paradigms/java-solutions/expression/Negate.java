package expression;

public class Negate extends Operation implements TripleExpression {
    String type;
    public Negate(Operation first) {
        this.first = first;
    }
    public Negate(Operation first, String type) {
        this.first = first;
        this.type = type;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int arg = first.evaluate(x, y, z);
        return - arg;
    }

    @Override
    public String toString() {
        if (type != null && type.equals("brackets"))
            return "-(" + first.toString() + ")";
        return "-" + first.toString();
    }

    public boolean equals(Operation operation) {
        return operation instanceof Negate &&
                this.first.equals(operation.first);
    }
}