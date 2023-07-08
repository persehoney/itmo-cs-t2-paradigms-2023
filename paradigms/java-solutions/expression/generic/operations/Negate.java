package expression.generic.operations;

import expression.exceptions.OverflowException;
import expression.generic.modes.Mode;

public class Negate<T extends Number> extends Operation<T> {
    String type;
    public Negate(Operation<T> first, Mode<T> mode) {
        this.first = first;
        this.mode = mode;
    }
    public Negate(Operation<T> first, String type, Mode<T> mode) {
        this.first = first;
        this.type = type;
        this.mode = mode;
    }

    @Override
    public T evaluate(T x, T y, T z) throws OverflowException {
        T arg = first.evaluate(x, y, z);
        return mode.negate(arg);
    }

    @Override
    public String toString() {
        if (type != null && type.equals("brackets"))
            return "-(" + first.toString() + ")";
        return "-" + first.toString();
    }
}