package expression.generic.modes;

public class DoubleMode extends Mode<Double> {
    public DoubleMode() {
    }

    public Double add(Double first, Double second) {
        return first + second;
    }

    @Override
    public Double subtract(Double first, Double second) {
        return first - second;
    }

    @Override
    public Double multiply(Double first, Double second) {
        return first * second;
    }

    @Override
    public Double divide(Double first, Double second) {
        return first / second;
    }

    @Override
    public Double negate(Double first) {
        return -first;
    }

    @Override
    public Double parseNumber(String s) {
        return Double.parseDouble(s);
    }

    @Override
    public String constToString(Double constant) {
        return Double.toString(constant);
    }
}
