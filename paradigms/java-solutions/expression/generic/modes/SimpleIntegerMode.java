package expression.generic.modes;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class SimpleIntegerMode extends Mode<Integer> {
    public SimpleIntegerMode() {
    }

    public Integer add(Integer first, Integer second) throws OverflowException {
        return first + second;
    }

    @Override
    public Integer subtract(Integer first, Integer second) {
        return first - second;
    }

    @Override
    public Integer multiply(Integer first, Integer second) {
        return first * second;
    }

    @Override
    public Integer divide(Integer first, Integer second) throws DivisionByZeroException {
        if (second == 0)
            throw new DivisionByZeroException();
        return first / second;
    }

    @Override
    public Integer negate(Integer first) {
        return - first;
    }

    @Override
    public Integer parseNumber(String s) {
        return Integer.parseInt(s);
    }

    @Override
    public String constToString(Integer constant) {
        return Integer.toString(constant);
    }
}
