package expression.generic.modes;

import expression.exceptions.OverflowException;

public abstract class Mode<T extends Number> {
    public abstract T add(T first, T second) throws OverflowException;
    public abstract T subtract(T first, T second) throws OverflowException;
    public abstract T multiply(T first, T second) throws OverflowException;
    public abstract T divide(T first, T second) throws OverflowException;
    public abstract T negate(T first) throws OverflowException;
    public abstract T parseNumber(String s);
    public abstract String constToString(T constant);
}
