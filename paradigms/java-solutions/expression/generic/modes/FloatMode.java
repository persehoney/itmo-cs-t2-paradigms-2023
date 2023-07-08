package expression.generic.modes;

public class FloatMode extends Mode<Float> {
    public FloatMode() {
    }

    public Float add(Float first, Float second) {
        return first + second;
    }

    @Override
    public Float subtract(Float first, Float second) {
        return first - second;
    }

    @Override
    public Float multiply(Float first, Float second) {
        return first * second;
    }

    @Override
    public Float divide(Float first, Float second){
        return first / second;
    }

    @Override
    public Float negate(Float first) {
        return -first;
    }

    @Override
    public Float parseNumber(String s) {
        return Float.parseFloat(s);
    }

    @Override
    public String constToString(Float constant) {
        return Float.toString(constant);
    }
}
