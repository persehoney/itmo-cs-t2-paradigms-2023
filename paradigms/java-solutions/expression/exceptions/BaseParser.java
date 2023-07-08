package expression.exceptions;

public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch = 0xffff;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean eof() {
        return take(END);
    }

    protected void skipWhitespace() {
        while (Character.isWhitespace(ch) ) {
            take();
        }
    }

    protected void expectArgument() throws ParseExpressionException {
        if (eof()) {
            throw new MissingArgumentException(getPosition());
        }
        if (!Character.isWhitespace(ch) && ch != '-' && ch != '(') {
            throw new IllegalSymbolException(getPosition());
        }

    }

    protected int getPosition() {
        return source.position();
    }

    protected boolean isNumber() {
        return '0' <= ch && ch <= '9';
    }

    protected String buildNumber() {
        StringBuilder sb = new StringBuilder();
        while (isNumber()) {
            sb.append(take());
        }
        return sb.toString();
    }

    protected void expect(final char expected) throws IllegalSymbolException {
        if (!take(expected)) {
            throw new IllegalSymbolException(getPosition());
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }
}
