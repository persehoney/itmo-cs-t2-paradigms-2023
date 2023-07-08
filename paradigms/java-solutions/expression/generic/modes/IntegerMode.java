package expression.generic.modes;

import expression.exceptions.OverflowException;

public class IntegerMode extends Mode<Integer> {
    public IntegerMode() {
    }

    public Integer add(Integer first, Integer second) throws OverflowException {
        if (first < 0 && second < 0 && second < Integer.MIN_VALUE - first
                || first > 0 && second > 0 && second > Integer.MAX_VALUE - first) {
            throw new OverflowException(first, second, '+');
        }
        return first + second;
    }

    @Override
    public Integer subtract(Integer first, Integer second) throws OverflowException {
        if (first < 0 && second > 0 && first < Integer.MIN_VALUE + second ||
                first > 0 && second < 0 && first > Integer.MAX_VALUE + second ||
                first == 0 && second == Integer.MIN_VALUE) {
            throw new OverflowException(first, second, '-');
        }
        return first - second;
    }

    @Override
    public Integer multiply(Integer first, Integer second) throws OverflowException {
        if (first > 0 && second > 0 && first > Integer.MAX_VALUE / second ||
                first < 0 && second < 0 && first < Integer.MAX_VALUE / second ||
                first < 0 && second > 0 && first < Integer.MIN_VALUE / second ||
                first > 0 && second < 0 && second < Integer.MIN_VALUE / first)
            throw new OverflowException(first, second, '*');
        return first * second;
    }

    @Override
    public Integer divide(Integer first, Integer second) throws OverflowException {
        if (first == Integer.MIN_VALUE && second == -1 || second == 0)
            throw new OverflowException(first, second, '/');
        return first / second;
    }

    @Override
    public Integer negate(Integer first) throws OverflowException {
        if (first == Integer.MIN_VALUE) {
            throw new OverflowException(first);
        }
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
