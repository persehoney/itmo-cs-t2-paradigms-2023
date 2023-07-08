package expression.generic.modes;

import expression.exceptions.OverflowException;

import java.math.BigInteger;

public class BigIntegerMode extends Mode<BigInteger> {
    public BigIntegerMode() {
    }

    public BigInteger add(BigInteger first, BigInteger second) {
        return first.add(second);
    }

    @Override
    public BigInteger subtract(BigInteger first, BigInteger second) throws OverflowException {
        return first.subtract(second);
    }

    @Override
    public BigInteger multiply(BigInteger first, BigInteger second) throws OverflowException {
        return first.multiply(second);
    }

    @Override
    public BigInteger divide(BigInteger first, BigInteger second) {
        if (second.equals(BigInteger.ZERO))
            throw new OverflowException(first, second, '/');
        return first.divide(second);
    }

    @Override
    public BigInteger negate(BigInteger first) {
        return first.negate();
    }

    @Override
    public BigInteger parseNumber(String s) {
        return new BigInteger(s);
    }

    @Override
    public String constToString(BigInteger constant) {
        return constant.toString();
    }
}
