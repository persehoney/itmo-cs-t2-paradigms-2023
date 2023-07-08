package expression.generic.operations;

import expression.generic.modes.Mode;

public class Const<T extends Number> extends Operation<T> {
    T constant;

    public Const(T constant, Mode<T> mode) {
        this.constant = constant;
        this.mode = mode;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return constant;
    }

    @Override
    public String toString() {
        return mode.constToString(constant);
    }
}

