package expression.generic;

import expression.exceptions.*;
import expression.generic.modes.IntegerMode;
import expression.generic.modes.Mode;
import expression.generic.operations.*;

public final class ExpressionParser<T extends Number> {
    Mode<T> mode;
    public ExpressionParser(Mode<T> mode) {
        this.mode = mode;
    }

    public Operation<T> parse(String source) throws ParseExpressionException {
        CorrectBracketSeqCheck(source);
        return parse(new StringSource(source));
    }

    private void CorrectBracketSeqCheck(String source) throws IncorrectBracketSeqException {
        int bracketCount = 0;
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == '(') {
                bracketCount++;
            } else if (source.charAt(i) == ')') {
                bracketCount--;
            }
            if (bracketCount < 0) {
                throw new IncorrectBracketSeqException();
            }
        }
        if (bracketCount != 0)
            throw new IncorrectBracketSeqException();
    }

    public Operation<T> parse(final CharSource source) {
        return new Parser(source).parseExpression();
    }

    private class Parser extends BaseParser {
        public Parser(CharSource source) {
            super(source);
        }

        char prevCh = 0xffff;
        boolean bracketsClose = false;

        public Operation<T> parseExpression() {
            Operation<T> prevOp = null;

            while (!eof() && !bracketsClose) {
                prevOp = parseElement(prevOp);
            }
            bracketsClose = false;
            return prevOp;
        }

        private Operation<T> parseElement(Operation<T> prevOp) {
            skipWhitespace();
            final Operation<T> result = parseValue(prevOp);
            skipWhitespace();
            return result;
        }

        private Operation<T> parseValue(Operation<T> prevOp) throws ParseExpressionException {
            if (test('x') || test('y') || test('z')) {
                return parseVariable();
            } else if (isNumber()) {
                return parseConst();
            } else if (test('+')) {
                return parseAdd(prevOp);
            } else if (test('*')) {
                return parseMultiply(prevOp);
            } else if (test('-') && (prevCh == 'v' || prevCh == ')')) {
                return parseSubtract(prevOp);
            } else if (test('/')) {
                return parseDivide(prevOp);
            } else if (test('-')) {
                return parseNegate(prevOp);
            } else if (test('(')) {
                return openBracket();
            } else if (test(')')) {
                return closeBracket(prevOp);
            }
            if (eof()) {
                throw new MissingArgumentException(getPosition());
            }
            throw new IllegalSymbolException(getPosition());
        }

        private Operation<T> parseVariable() {
            hasOperation(prevCh);
            char var = take();
            skipWhitespace();

            if (prevCh == '+' || prevCh == '-') {
                checkPriority(new Variable<>(Character.toString(var)));
            }

            prevCh = 'v';
            return new Variable<>(Character.toString(var));
        }

        private Operation<T> parseConst() throws OverflowException {
            hasOperation(prevCh);
            String number = buildNumber();

            if (prevCh == 'u' && number.equals("2147483648") && mode instanceof IntegerMode) {
                prevCh = 'v';
                return new Const<>(mode.parseNumber("-" + number), mode);
            } else if (prevCh == '+' || prevCh == '-') {
                return checkPriority(new Const<>(mode.parseNumber(number), mode));
            }

            try {
                prevCh = 'v';
                return new Const<>(mode.parseNumber(number), mode);
            } catch (NumberFormatException e) {
                if (mode instanceof IntegerMode)
                    throw new OverflowException(number);
                return new Const<>(mode.parseNumber("-" + number), mode);
            }
        }

        private Operation<T> parseAdd(Operation<T> prevOp) {
            hasPrevArgument(prevCh);
            prevCh = take();
            return new Add<>(prevOp, parseElement(prevOp), mode);
        }

        private Operation<T> parseMultiply(Operation<T> prevOp) {
            hasPrevArgument(prevCh);
            prevCh = take();
            return new Multiply<>(prevOp, parseElement(prevOp), mode);
        }

        private Operation<T> parseSubtract(Operation<T> prevOp) {
            hasPrevArgument(prevCh);
            prevCh = take();
            return new Subtract<>(prevOp, parseElement(prevOp), mode);
        }

        private Operation<T> parseDivide(Operation<T> prevOp) {
            hasPrevArgument(prevCh);
            prevCh = take();
            return new Divide<>(prevOp, parseElement(prevOp), mode);
        }

        private Operation<T> parseNegate(Operation<T> prevOp) {
            prevCh = 'u';
            take();

            skipWhitespace();

            if (isNumber()) {
                Operation<T> operation = parseConst();
                if (operation.toString().equals("-2147483648")) {
                    return operation;
                } else {
                    return new Negate<>(operation, mode);
                }
            }

            if (test('(')) {
                return new Negate<>(openBracket(), "brackets", mode);
            }
            return new Negate<>(parseElement(prevOp), mode);
        }

        private Operation<T> openBracket() throws IllegalSymbolException {

            if (prevCh == 'v' || prevCh == ')') {
                throw new IllegalSymbolException(getPosition());
            }

            char opBeforeBracket = prevCh;
            prevCh = take();

            Operation<T> op = parseExpression();

            if (opBeforeBracket == '+' || opBeforeBracket == '-') {
                if (test('*')) {
                    return parseMultiply(op);
                }
                else if (test('/')) {
                    return parseDivide(op);
                }
            }

            return op;
        }

        private Operation<T> closeBracket(Operation<T> prevOp) {
            hasPrevArgument(prevCh);

            prevCh = take();
            bracketsClose = true;
            return prevOp;
        }

        private void hasPrevArgument(char ch) throws MissingArgumentException {
            if (ch != 'v' && ch != ')') {
                throw new MissingArgumentException(getPosition());
            }
        }

        private void hasOperation(char ch) throws MissingArgumentException {
            if (ch == 'v' || ch == ')')
                throw new MissingArgumentException(getPosition());
        }

        private Operation<T> checkPriority(Operation<T> operation) {
            prevCh = 'v';
            skipWhitespace();

            while (test('*') || test('/')) {
                if (test('*')) {
                    operation = parseMultiply(operation);
                }
                else if (test('/')) {
                    operation = parseDivide(operation);
                }
                skipWhitespace();
            }

            return operation;
        }
    }
}