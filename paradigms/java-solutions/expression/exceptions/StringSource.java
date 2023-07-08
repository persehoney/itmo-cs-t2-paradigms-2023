package expression.exceptions;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(String data) {
        this.data = data;
    }

    public boolean hasNext() {
        return pos < data.length();
    }

    public char next() {
        return data.charAt(pos++);
    }

    public int position() { return pos; }
}
