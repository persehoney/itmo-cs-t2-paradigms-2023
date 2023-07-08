package expression.generic.modes;

import expression.exceptions.DivisionByZeroException;

public class ShortMode extends Mode<Short> {
    public ShortMode() {
    }

    public Short add(Short first, Short second) {
        return (short) (first + second);
    }

    @Override
    public Short subtract(Short first, Short second) {
        return (short) (first - second);
    }

    @Override
    public Short multiply(Short first, Short second) {
        return (short) (first * second);
    }

    @Override
    public Short divide(Short first, Short second) throws DivisionByZeroException {
        if (second == 0)
            throw new DivisionByZeroException();
        return (short) (first / second);
    }

    @Override
    public Short negate(Short first) {
        return (short) -first;
    }

    @Override
    public Short parseNumber(String s) {
        return (short) Integer.parseInt(s);
    }

    @Override
    public String constToString(Short constant) {
        return Short.toString(constant);
    }
}
