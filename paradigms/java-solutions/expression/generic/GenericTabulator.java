package expression.generic;

import expression.exceptions.EvaluateExpressionException;
import expression.exceptions.NoSuchModeException;
import expression.exceptions.ParseExpressionException;
import expression.generic.modes.*;
import expression.generic.operations.Operation;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)
            throws NoSuchModeException, EvaluateExpressionException, ParseExpressionException {
        return make_table(getMode(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private Mode<?> getMode(String modeName) {
        switch (modeName) {
            case "d" -> {
                return new DoubleMode();
            }
            case "bi" -> {
                return new BigIntegerMode();
            }
            case "i" -> {
                return new IntegerMode();
            }
            case "u" -> {
                return new SimpleIntegerMode();
            }
            case "f" -> {
                return new FloatMode();
            }
            case "s" -> {
                return new ShortMode();
            }
            default -> throw new NoSuchModeException(modeName);
        }
    }

    private <T extends Number> Object[][][] make_table(Mode<T> mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        ExpressionParser<T> parser = new ExpressionParser<>(mode);

        Operation<T> operation = parser.parse(expression);

        Object[][][] table = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = 0; i < x2 - x1 + 1; i++) {
            for (int j = 0; j < y2 - y1 + 1; j++) {
                for (int k = 0; k < z2 - z1 + 1; k++) {
                    try {
                        table[i][j][k] = operation.evaluate(
                                mode.parseNumber(Integer.toString(x1 + i)),
                                mode.parseNumber(Integer.toString(y1 + j)),
                                mode.parseNumber(Integer.toString(z1 + k)));
                    } catch (EvaluateExpressionException e) {
                        table[i][j][k] = null;
                    }
                }
            }
        }
        return table;
    }
}
