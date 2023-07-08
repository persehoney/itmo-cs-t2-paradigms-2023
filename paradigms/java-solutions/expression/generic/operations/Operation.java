package expression.generic.operations;

import expression.generic.modes.Mode;

public abstract class Operation<T extends Number> {
    public Operation<T> first;
    public Operation<T> second;
    public Mode<T> mode;

    public Operation(Operation<T> first, Operation<T> second, Mode<T> mode) {
        this.first = first;
        this.second = second;
        this.mode = mode;
    }

    public Operation() {
    }

    public abstract T evaluate(T x, T y, T z);

    public abstract String toString();
}
