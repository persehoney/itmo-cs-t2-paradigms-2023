package expression.generic.operations;

import expression.exceptions.OverflowException;
import expression.generic.modes.Mode;

public class Add<T extends Number> extends Operation<T> {
    public Add(Operation<T> first, Operation<T> second, Mode<T> mode) {
        super(first, second, mode);
    }

    @Override
    public T evaluate(T x, T y, T z) throws OverflowException {
        T arg1 = first.evaluate(x, y, z);
        T arg2 = second.evaluate(x, y, z);

        return mode.add(arg1, arg2);
    }

    @Override
    public String toString() {
        return "(" + first.toString() + " + " + second.toString() + ")";
    }
}