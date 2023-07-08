package expression.exceptions;

import expression.*;

public final class ExpressionParser implements TripleParser {
    public TripleExpression parse(String source) throws ParseExpressionException {
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

    public static Operation parse(final CharSource source) {
        return new Parser(source).parseExpression();
    }

    private static class Parser extends BaseParser {
        public Parser(CharSource source) {
            super(source);
        }

        char prevCh = 0xffff;
        boolean bracketsClose = false;

        public Operation parseExpression() {
            Operation prevOp = null;

            while (!eof() && !bracketsClose) {
                prevOp = parseElement(prevOp);
            }
            bracketsClose = false;
            return prevOp;
        }

        private Operation parseElement(Operation prevOp) {
            skipWhitespace();
            final Operation result = parseValue(prevOp);
            skipWhitespace();
            return result;
        }

        private Operation parseValue(Operation prevOp) throws ParseExpressionException {
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
            } else if (test('c')) {
                take();
                if (test('o')) {
                    expect("ount");
                    return parseCount();
                } else if (test('l')) {
                    expect("lear");
                    return parseClear(prevOp);
                }
                throw new IllegalSymbolException(getPosition());
            } else if (test('s')) {
                expect("set");
                return parseSet(prevOp);
            }
            if (eof()) {
                throw new MissingArgumentException(getPosition());
            }
            throw new IllegalSymbolException(getPosition());
        }

        private Operation parseVariable() {
            hasOperation(prevCh);
            char var = take();
            skipWhitespace();

            if (prevCh == '+' || prevCh == '-') {
                checkPriority(new Variable(Character.toString(var)));
            }

            prevCh = 'v';
            return new Variable(Character.toString(var));
        }

        private Operation parseConst() throws OverflowException {
            hasOperation(prevCh);
            String number = buildNumber();

            if (prevCh == 'u' && number.equals("2147483648")) {
                prevCh = 'v';
                return new Const(Integer.parseInt("-" + number));
            } else if (prevCh == '+' || prevCh == '-') {
                return checkPriority(new Const(Integer.parseInt(number)));
            }

            try {
                prevCh = 'v';
                return new Const(Integer.parseInt(number));
            } catch (NumberFormatException e) {
                throw new OverflowException(number);
            }
        }

        private Operation parseAdd(Operation prevOp) {
            hasPrevArgument(prevCh);
            prevCh = take();
            return new CheckedAdd(prevOp, parseElement(prevOp));
        }

        private Operation parseMultiply(Operation prevOp) {
            hasPrevArgument(prevCh);
            prevCh = take();
            return new CheckedMultiply(prevOp, parseElement(prevOp));
        }

        private Operation parseSubtract(Operation prevOp) {
            hasPrevArgument(prevCh);
            prevCh = take();
            return new CheckedSubtract(prevOp, parseElement(prevOp));
        }

        private Operation parseDivide(Operation prevOp) {
            hasPrevArgument(prevCh);
            prevCh = take();
            return new CheckedDivide(prevOp, parseElement(prevOp));
        }

        private Operation parseNegate(Operation prevOp) {
            prevCh = 'u';
            take();

            skipWhitespace();

            if (isNumber()) {
                Operation operation = parseConst();
                if (operation.toString().equals("-2147483648")) {
                    return operation;
                } else {
                    return new CheckedNegate(operation);
                }
            }

            if (test('(')) {
                return new CheckedNegate(openBracket(), "brackets");
            }
            return new CheckedNegate(parseElement(prevOp));
        }

        private Operation openBracket() throws IllegalSymbolException {

            if (prevCh == 'v' || prevCh == ')') {
                throw new IllegalSymbolException(getPosition());
            }

            char opBeforeBracket = prevCh;
            prevCh = take();

            Operation op = parseExpression();

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

        private Operation closeBracket(Operation prevOp) {
            hasPrevArgument(prevCh);

            prevCh = take();
            bracketsClose = true;
            return prevOp;
        }

        private Operation parseCount() {
            expectArgument();
            return new Count(openBracket());
        }

        private Operation parseSet(Operation prevOp) {
            expectArgument();
            hasPrevArgument(prevCh);
            prevCh = 's';
            return new Set(prevOp, parseElement(prevOp));
        }

        private Operation parseClear(Operation prevOp) {
            expectArgument();
            hasPrevArgument(prevCh);
            prevCh = 'c';
            return new Clear(prevOp, parseElement(prevOp));
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

        private Operation checkPriority(Operation operation) {
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