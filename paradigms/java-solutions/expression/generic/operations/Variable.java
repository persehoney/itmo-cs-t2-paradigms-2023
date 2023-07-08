package expression.generic.operations;

public class Variable<T extends Number> extends Operation<T> {
    final String var;

    public Variable(String var) {
        this.var = var;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        if (var.equals("x")) return x;
        else if (var.equals("y")) return y;
        else return z;
    }

    @Override
    public String toString() {
        return var;
    }
}