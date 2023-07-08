package expression;

public class Const extends Operation {
    int constant;

    public Const(int constant) {
        this.constant = constant;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return constant;
    }

    @Override
    public String toString() {
        return Integer.toString(constant);
    }

    public boolean equals(Operation operation) {
        return operation instanceof Const && this.constant == ((Const) operation).constant;
    }
}

