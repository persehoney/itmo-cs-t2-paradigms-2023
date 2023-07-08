package expression;

public class Variable extends Operation implements TripleExpression {
    final String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (var.equals("x")) return x;
        else if (var.equals("y")) return y;
        else return z;
    }

    @Override
    public String toString() {
        return var;
    }

    public boolean equals(Operation operation) {
        return operation instanceof Variable && this.var.equals(((Variable) operation).var);
    }
}